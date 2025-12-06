<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>用户登录 - 二手物品交易平台</title>
    <link rel="stylesheet" href="../css/style.css">
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
    
    <script>
        // 显示成功消息（如果有）
        const urlParams = new URLSearchParams(window.location.search);
        const success = urlParams.get('success');
        if (success) {
            document.getElementById('successMessage').textContent = decodeURIComponent(success);
            document.getElementById('successMessage').style.display = 'block';
        }
        
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            // 隐藏之前的消息
            document.getElementById('errorMessage').style.display = 'none';
            document.getElementById('successMessage').style.display = 'none';
            
            // 获取表单数据
            const formData = new FormData(this);
            
            // 发送登录请求
            fetch('<%=request.getContextPath()%>/login', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 显示成功消息
                    document.getElementById('successMessage').textContent = data.message;
                    document.getElementById('successMessage').style.display = 'block';
                    
                    // 1秒后跳转到首页
                    setTimeout(() => {
                        window.location.href = '<%=request.getContextPath()%>/products';
                    }, 1000);
                } else {
                    // 显示错误消息
                    document.getElementById('errorMessage').textContent = data.message;
                    document.getElementById('errorMessage').style.display = 'block';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('errorMessage').textContent = '登录时发生错误，请稍后重试';
                document.getElementById('errorMessage').style.display = 'block';
            });
        });
    </script>
</body>
</html>