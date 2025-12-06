<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>论坛系统 - 创建帖子</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="header">
        <h1>论坛系统</h1>
        <div class="user-info">
            欢迎, <span id="username"></span>!
            <a href="home.jsp">首页</a>
            <a href="javascript:void(0)" id="logoutLink">退出</a>
        </div>
    </div>
    
    <div class="container">
        <div class="form-container">
            <h2>创建新帖子</h2>
            
            <div id="errorMessage" class="error-message"></div>
            
            <form id="createThreadForm">
                <div class="form-group">
                    <label for="title">标题:</label>
                    <input type="text" id="title" name="title" required>
                </div>
                
                <div class="form-group">
                    <label for="content">内容:</label>
                    <textarea id="content" name="content" required></textarea>
                </div>
                
                <div class="form-group">
                    <input type="submit" value="发布帖子" class="btn">
                    <a href="home.jsp" class="btn btn-cancel">取消</a>
                </div>
            </form>
        </div>
    </div>
    
    <script src="js/main.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            checkLoginStatus();
            
            document.getElementById('createThreadForm').addEventListener('submit', function(e) {
                e.preventDefault();
                createThread();
            });
            
            document.getElementById('logoutLink').addEventListener('click', logout);
            
            const urlParams = new URLSearchParams(window.location.search);
            const error = urlParams.get('error');
            
            if (error) {
                document.getElementById('errorMessage').textContent = decodeURIComponent(error);
            }
        });
    </script>
</body>
</html>