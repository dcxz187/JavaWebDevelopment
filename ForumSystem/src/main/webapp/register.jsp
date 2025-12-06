<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>论坛系统 - 注册</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="register-form">
            <h1>用户注册</h1>
            
            <div id="errorMessage" class="error-message"></div>
            
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
                    <label for="captcha">验证码:</label>
                    <div class="captcha-group">
                        <input type="text" id="captcha" name="captcha" required>
                        <img id="captchaImg" src="api/captcha" class="captcha-img" alt="验证码">
                        <button type="button" id="refreshCaptcha" class="refresh-btn">刷新</button>
                    </div>
                </div>
                
                <div class="form-group">
                    <input type="submit" value="注册" class="btn btn-success">
                </div>
            </form>
            
            <div class="links">
                <a href="login.jsp">已有账号？立即登录</a>
            </div>
        </div>
    </div>
    
    <script src="js/main.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // 刷新验证码
            document.getElementById('refreshCaptcha').addEventListener('click', function() {
                document.getElementById('captchaImg').src = 'api/captcha?' + Math.random();
            });
            
            // 处理表单提交
            document.getElementById('registerForm').addEventListener('submit', function(e) {
                e.preventDefault();
                register();
            });
            
            // 检查URL参数中的错误消息
            const urlParams = new URLSearchParams(window.location.search);
            const error = urlParams.get('error');
            
            if (error) {
                document.getElementById('errorMessage').textContent = decodeURIComponent(error);
            }
        });
    </script>
</body>
</html>