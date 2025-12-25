以下是Java web课程的一个作业，请你完成，可以参考OnlineChatPoom项目的文件结构以及它的部署脚本
一个论坛/讨论网页
用户可以在上面进行提问讨论，其他人都可以回复
要求：
mvc骨架
强制不要做i/o
选择合适的数据存储方案
需要安全登陆（设计一个基于自己编写的验证码验证的过程，用纯java web 的contentype相关知识来实现，非第三方框架或库调用）
可以查看所有的讨论信息列表，可以点击某个信息，查看详细内容以及该讨论话题相关的回复（可以多个），每个讨论话题是一个独立的thread。
每个讨论都可以是多行文本，正常换行显示
适当美化页面。

# 论坛系统 - Spring重构版开发文档

## 项目概述

本项目是一个基于Spring框架重构的论坛系统，原项目使用纯Servlet/JSP实现，现使用Spring MVC和Spring IoC容器进行重构，提高了代码的可维护性和可扩展性。

## 项目结构

```
ForumSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/forum/
│       │       ├── config/           # 配置类
│       │       │   ├── AppConfig.java
│       │       │   ├── WebConfig.java
│       │       │   └── WebAppInitializer.java
│       │       ├── controller/       # Spring MVC控制器
│       │       │   ├── AuthController.java     # 认证相关（登录、注册、登出）
│       │       │   ├── CaptchaController.java  # 验证码
│       │       │   └── ForumController.java    # 论坛功能（帖子、回复）
│       │       ├── dao/              # 数据访问层
│       │       │   └── DataStore.java
│       │       ├── model/            # 数据模型
│       │       │   ├── Reply.java
│       │       │   ├── Thread.java
│       │       │   └── User.java
│       │       └── service/          # 业务逻辑层
│       │           ├── impl/         # 服务实现
│       │           │   ├── ReplyServiceImpl.java
│       │           │   ├── ThreadServiceImpl.java
│       │           │   └── UserServiceImpl.java
│       │           ├── ReplyService.java
│       │           ├── ThreadService.java
│       │           └── UserService.java
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml
│           ├── css/
│           ├── js/
│           ├── createThread.jsp
│           ├── home.jsp
│           ├── login.jsp
│           ├── register.jsp
│           └── thread.jsp
├── pom.xml
└── README.md
```

## 技术架构

- **Spring Framework 6.0.0**: 核心框架，提供IoC容器和AOP支持
- **Spring MVC**: Web层框架，处理HTTP请求和响应
- **Jakarta EE**: 提供Servlet、JSP等Web技术
- **Gson**: JSON处理

## 重构要点

### 1. 控制器合并
- 将多个功能相似的控制器合并为两个主要控制器：
  - [AuthController](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/ForumSystem/src/main/java/com/forum/controller/AuthController.java#L11-L180): 处理用户认证相关功能（登录、注册、登出、获取用户信息）
  - [ForumController](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/ForumSystem/src/main/java/com/forum/controller/ForumController.java#L12-L190): 处理论坛核心功能（帖子、回复）

### 2. Service层重构
- 将Service实现类移至[impl](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/ForumSystem/src/main/java/com/forum/service/impl)包，遵循分层架构规范
- 通过接口定义实现松耦合设计

### 3. 依赖注入
- 使用[@Autowired](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/ForumSystem/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst#L22-L22)注解实现依赖注入
- 使用[@Service](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/ForumSystem/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst#L22-L22)注解标识服务组件
- 使用[@Controller](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/ForumSystem/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst#L22-L22)注解标识控制器组件

## API接口

### 认证相关
- `POST /api/login` - 用户登录
- `POST /api/register` - 用户注册
- `POST /api/logout` - 用户登出
- `GET /api/user` - 获取当前用户信息

### 论坛功能
- `GET /api/threads` - 获取帖子列表
- `GET /api/thread?id={id}` - 获取帖子详情
- `POST /api/createThread` - 创建帖子
- `POST /api/addReply` - 添加回复

### 其他
- `GET /api/captcha` - 获取验证码图片

## 配置说明

- 使用Java配置类替代XML配置
- 通过[WebAppInitializer](file:///Users/DaYang/ProjectCode/JavaDevelopProject/JavaWebDevelopment/ForumSystem/src/main/java/com/forum/config/WebAppInitializer.java#L7-L21)配置Spring MVC的DispatcherServlet
- 视图解析器配置保留原有JSP页面结构