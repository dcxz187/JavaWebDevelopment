<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>用户注册 - 二手物品交易平台</title>
    <link rel="stylesheet" href="../css/style.css">
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
    
    <script>
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            // 隐藏之前的消息
            document.getElementById('errorMessage').style.display = 'none';
            document.getElementById('successMessage').style.display = 'none';
            
            // 获取表单数据
            const formData = new FormData(this);
            
            // 发送注册请求
            fetch('<%=request.getContextPath()%>/register', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 显示成功消息
                    document.getElementById('successMessage').textContent = data.message;
                    document.getElementById('successMessage').style.display = 'block';
                    
                    // 3秒后跳转到登录页面
                    setTimeout(() => {
                        window.location.href = '<%=request.getContextPath()%>/login';
                    }, 3000);
                } else {
                    // 显示错误消息
                    document.getElementById('errorMessage').textContent = data.message;
                    document.getElementById('errorMessage').style.display = 'block';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('errorMessage').textContent = '注册时发生错误，请稍后重试';
                document.getElementById('errorMessage').style.display = 'block';
            });
        });
    </script>
</body>
</html>