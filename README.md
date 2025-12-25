# Java Web 开发作业集

这是一个包含多个Java Web应用程序的作业集合，涵盖了不同类型的Web应用开发实践。

## 项目列表

### 1. ForumSystem (论坛系统)

一个基于Spring框架重构的在线论坛系统，支持用户注册登录、帖子发布和回复功能。

访问地址：http://10.100.164.35:8080/ForumSystem-2.0/

#### 功能特点
- 用户注册和登录功能
- 验证码验证机制
- 帖子发布和管理
- 回复功能
- MVC架构模式
- 基于Spring框架实现
- 支持时间格式化显示

#### 技术栈
- Java Servlet/JSP
- Spring Framework (Spring MVC, Spring IoC)
- JSTL标签库
- Jackson (JSON处理)
- Jakarta EE

#### 主要组件
- **Model层**: User(用户), Thread(主题帖), Reply(回复)
- **DAO层**: DataStore(数据存储)
- **Controller层**: 多个控制器处理不同请求(认证、验证码、论坛功能等)
- **Service层**: 业务逻辑处理
- **View层**: JSP页面展示数据

#### 测试用账号
- 用户名: admin
- 密码: admin123


### 2. OnlineChatRoom (在线聊天室)

一个功能丰富的在线聊天室应用，支持公聊和私聊功能。

访问地址：http://10.100.164.35:8080/OnlineChatRoom2.0/

#### 功能特点
- 用户登录和会话管理
- 实时公共聊天和私聊功能
- 在线用户列表显示
- 心跳检测保持连接状态
- 系统消息通知（用户加入/离开）
- XSS防护
- 私聊功能
- 自动消息轮询更新

#### 技术栈
- Java Servlet
- Gson (JSON处理)
- Lombok (简化Java代码)
- HTML/CSS/JavaScript
- AJAX通信

#### 核心组件
- **Controller层**: 多个Servlet控制器处理不同请求（登录、聊天、消息发送等）
- **Filter层**: 身份验证过滤器确保安全访问
- **Listener层**: 会话监听器管理用户连接状态
- **Model层**: 消息和响应模型
- **Util层**: 会话管理工具

### 3. SecondHandPlatform (二手物品交易平台)

一个基于Java Web技术构建的二手物品交易平台，满足课程作业要求。

访问地址：http://10.100.164.35:8080/SecondHandPlatform/

#### 功能特点
- 用户注册和登录功能（密码加密存储）
- 二手物品发布、浏览、搜索功能
- 物品信息的增删改查操作
- 模糊匹配搜索功能
- MVC架构模式

#### 技术栈
- Java Servlet/JSP
- JSTL标签库
- MySQL数据库
- HikariCP数据库连接池
- BCrypt密码加密
- Gson (JSON处理)
- 原生HTML/CSS/JavaScript

#### 项目结构

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

#### 数据库设计

 用户表 (users)
- id: 用户ID（主键，自增）
- username: 用户名（唯一，非空）
- password: 密码（加密存储，非空）
- created_at: 创建时间

 物品表 (products)
- id: 物品ID（主键，自增）
- name: 物品名称（非空）
- description: 物品描述
- price: 价格
- owner_id: 所有者ID（外键，关联users表）
- created_at: 创建时间
- updated_at: 更新时间

#### 默认测试账户
- 用户名: admin, 密码: 123456
- 

### 4. QuestionnaireApp (问卷调查页面)

一个用于创建和填写问卷调查的Web应用。

访问地址：http://10.100.164.35:8080/QuestionnaireApp/

#### 功能特点
- 问卷创建和展示
- 结果统计和显示
- 数据处理

#### 技术栈
- Java Servlet
- Gson (JSON处理)
- HTML/CSS/JavaScript

### 5. SLlibraryIndex (图书馆索引系统)

模拟图书馆网站首页

访问地址：http://10.100.164.35:8080/SLlibraryIndex/

#### 功能特点
- 图书馆网站布局
- 异步搜索功能
- 搜索历史记录
- 响应式导航栏

#### 技术要求
- 全程手写，不使用第三方前端框架
- CSS、JS与HTML分离
- 鼠标悬停效果
- 异步搜索功能

#### 技术栈
- Java Servlet
- Gson (JSON处理)
- 原生HTML/CSS/JavaScript
- AJAX异步请求

### 6. SinglesDayShopping (双十一购物页面)

一个模拟双十一购物节的电商网站页面。

访问地址：http://10.100.164.35:8080/SinglesDayShopping/

#### 功能特点
- 商品展示
- 购物车功能
- 产品列表

#### 技术栈
- Java Servlet
- Gson (JSON处理)
- HTML/CSS/JavaScript

## 构建和运行

所有项目都是Maven项目，使用以下命令进行构建：

```bash
# 进入具体项目目录后执行
mvn clean package
```

生成的WAR文件可部署到支持Jakarta EE的Web容器中，如Tomcat 10+。

## 系统要求

- Java 17+
- Maven 3.6+
- 支持Jakarta EE的Web服务器 (如 Tomcat 10+)

## 依赖项

所有项目共享以下主要依赖：

- jakarta.servlet:jakarta.servlet-api:6.0.0 (provided)
- com.google.code.gson:gson:2.10.1
