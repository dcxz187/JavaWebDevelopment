# Java Web 开发作业集

这是一个包含多个Java Web应用程序的作业集合，涵盖了不同类型的Web应用开发实践。

## 项目列表

### 1. ForumSystem (论坛系统)

一个基于Java Web技术构建的在线论坛系统。

访问地址：http://10.100.164.35:8080/ForumSystem/

#### 功能特点
- 用户注册和登录功能
- 验证码验证机制
- 帖子发布和管理
- 回复功能
- MVC架构模式
- 使用纯Java Web技术实现，无第三方框架

#### 技术栈
- Java Servlet/JSP
- JSTL标签库
- Gson (JSON处理)
- Jakarta EE

#### 主要组件
- **Model层**: User(用户), Thread(主题帖), Reply(回复)
- **DAO层**: DataStore(数据存储)
- **Servlet层**: 多个控制器处理不同请求(登录、注册、帖子创建、回复等)
- **View层**: JSP页面展示数据


### 2. OnlineChatRoom (在线聊天室)

一个简单的在线聊天室应用。
访问地址：http://10.100.164.35:8080/OnlineChatRoom/

#### 功能特点
- 实时消息传递
- 用户登录
- 聊天界面

#### 技术栈
- Java Servlet
- Gson (JSON处理)
- HTML/CSS/JavaScript
- AJAX通信

### 3. QuestionnaireApp (问卷调查页面)

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

### 4. SLlibraryIndex (图书馆索引系统)

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

### 5. SinglesDayShopping (双十一购物页面)

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