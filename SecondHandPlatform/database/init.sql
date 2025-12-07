-- 创建数据库
CREATE DATABASE IF NOT EXISTS secondhand_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE secondhand_platform;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建物品表
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2),
    owner_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 插入测试数据
INSERT IGNORE INTO users (username, password) VALUES 
('admin', '$2a$10$wHjp1ZW.19Ly9/b5/Fq1ZOc51i6oVHSvW6LwRpiG5e3CVsUt2zBkG'), -- 密码: admin123
('user1', '$2a$10$rZ7znSJpr5U9GsNhAH.0.uwtxNaZjME/qjq0aftBbhC1cG3Q9DpEq'), -- 密码: password1
('user2', '$2a$10$rZ7znSJpr5U9GsNhAH.0.uwtxNaZjME/qjq0aftBbhC1cG3Q9DpEq'); -- 密码: password1

INSERT IGNORE INTO products (name, description, price, owner_id) VALUES 
('智能手机', '九成新智能手机，几乎没用过', 1200.00, 1),
('笔记本电脑', '联想ThinkPad笔记本，办公利器', 3500.00, 1),
('山地自行车', '专业山地自行车，适合户外运动', 2200.00, 1),
('古典吉他', '雅马哈古典吉他，音色优美', 800.00, 1);