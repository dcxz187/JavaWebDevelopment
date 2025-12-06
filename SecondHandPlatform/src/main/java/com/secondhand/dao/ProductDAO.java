package com.secondhand.dao;

import com.secondhand.model.Product;
import com.secondhand.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品数据访问对象
 */
public class ProductDAO {
    
    /**
     * 获取所有物品列表
     * @return 物品列表
     */
    public List<Product> findAll() {
        return findByKeyword(null);
    }
    
    /**
     * 根据关键字搜索物品（模糊匹配）
     * @param keyword 搜索关键字
     * @return 匹配的物品列表
     */
    public List<Product> findByKeyword(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, u.username as owner_name FROM products p " +
                     "JOIN users u ON p.owner_id = u.id ";
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += "WHERE p.name LIKE ? OR p.description LIKE ? ";
        }
        
        sql += "ORDER BY p.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setOwnerId(rs.getInt("owner_id"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    product.setCreatedAt(createdAt.toLocalDateTime());
                }
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    product.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                
                // 设置关联的用户信息
                User owner = new User();
                owner.setUsername(rs.getString("owner_name"));
                product.setOwner(owner);
                
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
    
    /**
     * 根据ID查找物品
     * @param id 物品ID
     * @return 物品对象，如果不存在则返回null
     */
    public Product findById(int id) {
        String sql = "SELECT p.*, u.username as owner_name FROM products p " +
                     "JOIN users u ON p.owner_id = u.id WHERE p.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setOwnerId(rs.getInt("owner_id"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    product.setCreatedAt(createdAt.toLocalDateTime());
                }
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    product.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                
                // 设置关联的用户信息
                User owner = new User();
                owner.setUsername(rs.getString("owner_name"));
                product.setOwner(owner);
                
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 创建新物品
     * @param product 物品对象
     * @return 是否创建成功
     */
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getOwnerId());
            stmt.setTimestamp(5, Timestamp.valueOf(product.getCreatedAt()));
            stmt.setTimestamp(6, Timestamp.valueOf(product.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * 更新物品信息
     * @param product 物品对象
     * @return 是否更新成功
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(5, product.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * 删除物品
     * @param id 物品ID
     * @return 是否删除成功
     */
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * 根据用户ID查找其发布的物品
     * @param ownerId 用户ID
     * @return 物品列表
     */
    public List<Product> findByOwnerId(int ownerId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE owner_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setOwnerId(rs.getInt("owner_id"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    product.setCreatedAt(createdAt.toLocalDateTime());
                }
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    product.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
}