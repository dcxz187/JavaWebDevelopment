package com.secondhand.servlet;

import com.google.gson.Gson;
import com.secondhand.dao.ProductDAO;
import com.secondhand.model.Product;
import com.secondhand.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/editProduct")
public class EditProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // 未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // 获取物品ID参数
        String idParam = request.getParameter("id");
        
        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                int productId = Integer.parseInt(idParam);
                
                // 查询物品详情
                Product product = productDAO.findById(productId);
                
                if (product != null) {
                    // 检查用户是否有权限编辑该物品
                    if (!product.getOwnerId().equals(user.getId())) {
                        // 没有权限，重定向到首页
                        response.sendRedirect(request.getContextPath() + "/products");
                        return;
                    }
                    
                    request.setAttribute("product", product);
                    // 转发到编辑物品页面
                    request.getRequestDispatcher("/jsp/edit_product.jsp").forward(request, response);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            // ID格式不正确
        }
        
        // 物品不存在或ID无效，重定向到首页
        response.sendRedirect(request.getContextPath() + "/products");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // 创建响应结果
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (user == null) {
                result.put("success", false);
                result.put("message", "请先登录");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 获取请求参数
            String idParam = request.getParameter("id");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            
            // 检查参数是否完整
            if (idParam == null || name == null || description == null || priceStr == null) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 检查参数是否为空字符串
            if (idParam.trim().isEmpty() || name.trim().isEmpty() || description.trim().isEmpty() || priceStr.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            int productId = Integer.parseInt(idParam);
            
            // 查询物品详情
            Product product = productDAO.findById(productId);
            if (product == null) {
                result.put("success", false);
                result.put("message", "物品不存在");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 检查用户是否有权限编辑该物品
            if (!product.getOwnerId().equals(user.getId())) {
                result.put("success", false);
                result.put("message", "您没有权限编辑该物品");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 验证价格格式
            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    result.put("success", false);
                    result.put("message", "价格必须大于0");
                    response.getWriter().write(new Gson().toJson(result));
                    return;
                }
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "价格格式不正确");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 更新物品信息
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            
            // 保存到数据库
            if (productDAO.updateProduct(product)) {
                result.put("success", true);
                result.put("message", "物品更新成功");
            } else {
                result.put("success", false);
                result.put("message", "物品更新失败，请稍后重试");
            }
            
            response.getWriter().write(new Gson().toJson(result));
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "无效的物品ID格式");
            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新物品时发生错误: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(result));
        }
    }
}