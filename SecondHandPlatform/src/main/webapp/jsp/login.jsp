<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>用户登录 - 二手物品交易平台</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2>用户登录</h2>
            
            <div id="errorMessage" class="error-message" style="display:none;"></div>
            <div id="successMessage" class="success-message" style="display:none;"></div>
            
            <form id="loginForm">
                <div class="form-group">
                    <label for="username">用户名:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                
                <div class="form-group">
                    <label for="password">密码:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn">登录</button>
                    <a href="<%=request.getContextPath()%>/products" class="btn btn-cancel">取消</a>
                </div>
            </form>
            
            <p>还没有账户? <a href="<%=request.getContextPath()%>/register">立即注册</a></p>
        </div>
    </div>

    <script data-context-path="<%=request.getContextPath()%>" src="<%=request.getContextPath()%>/js/login.js"></script>
</body>
</html>