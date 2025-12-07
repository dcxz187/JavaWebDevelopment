<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>用户注册 - 二手物品交易平台</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2>用户注册</h2>
            
            <div id="errorMessage" class="error-message" style="display:none;"></div>
            <div id="successMessage" class="success-message" style="display:none;"></div>
            
            <form id="registerForm">
                <div class="form-group">
                    <label for="username">用户名:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                
                <div class="form-group">
                    <label for="password">密码:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">确认密码:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn">注册</button>
                    <a href="<%=request.getContextPath()%>/products" class="btn btn-cancel">取消</a>
                </div>
            </form>
            
            <p>已有账户? <a href="<%=request.getContextPath()%>/login">立即登录</a></p>
        </div>
    </div>

    <script data-context-path="<%=request.getContextPath()%>" src="<%=request.getContextPath()%>/js/register.js"></script>
</body>
</html>