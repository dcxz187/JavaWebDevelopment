package com.secondhand.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 二手物品实体类
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联的用户对象（非数据库字段）
    private User owner;

    public Product(String name, String description, BigDecimal price, Integer ownerId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ownerId = ownerId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Product(Integer id, String name, String description, BigDecimal price, Integer ownerId, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}