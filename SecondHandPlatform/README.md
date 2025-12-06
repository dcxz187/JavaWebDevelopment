# 二手物品交易平台

这是一个基于Java Web技术构建的二手物品交易平台，满足课程作业要求。

## 功能特点

- 用户注册和登录功能（密码加密存储）
- 二手物品发布、浏览、搜索功能
- 物品信息的增删改查操作
- 模糊匹配搜索功能
- MVC架构模式

## 技术栈

- Java Servlet/JSP
- JSTL标签库
- MySQL数据库
- HikariCP数据库连接池
- BCrypt密码加密
- Gson (JSON处理)
- 原生HTML/CSS/JavaScript

## 项目结构

```
SecondHandPlatform/
├── src/main/java/com/secondhand/
│   ├── model/              # 实体类
│   ├── dao/                # 数据访问对象
│   ├── servlet/            # 控制器
│   └── util/               # 工具类
├── src/main/webapp/
│   ├── WEB-INF/
│   ├── css/                # 样式文件
│   ├── js/                 # JavaScript文件
│   └── jsp/                # JSP页面
└── database/               # 数据库初始化脚本
```

## 数据库设计

### 用户表 (users)
- id: 用户ID（主键，自增）
- username: 用户名（唯一，非空）
- password: 密码（加密存储，非空）
- created_at: 创建时间

### 物品表 (products)
- id: 物品ID（主键，自增）
- name: 物品名称（非空）
- description: 物品描述
- price: 价格
- owner_id: 所有者ID（外键，关联users表）
- created_at: 创建时间
- updated_at: 更新时间

## 快速开始

1. 创建MySQL数据库并执行 [init.sql](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/SecondHandPlatform/database/init.sql) 脚本初始化数据表
2. 修改 [DatabaseConnection.java](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/SecondHandPlatform/src/main/java/com/secondhand/dao/DatabaseConnection.java) 中的数据库连接配置
3. 使用Maven构建项目：
   ```bash
   mvn clean package
   ```
4. 将生成的WAR包部署到支持Jakarta EE的Web容器中（如Tomcat 10+）

## 默认测试账户

- 用户名: admin, 密码: admin123
- 用户名: user1, 密码: password1
- 用户名: user2, 密码: password1