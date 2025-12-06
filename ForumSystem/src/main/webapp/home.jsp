<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>论坛系统 - 首页</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="header">
        <h1>论坛系统</h1>
        <div class="user-info">
            欢迎, <span id="username"></span>!
            <a href="createThread.jsp">创建帖子</a>
            <a href="javascript:void(0)" id="logoutLink">退出</a>
        </div>
    </div>
    
    <div class="container">
        <div class="threads">
            <div class="thread-header">
                <h2>所有帖子</h2>
            </div>
            
            <div id="threadsList">
                <!-- 帖子列表将通过JavaScript动态加载 -->
            </div>
        </div>
    </div>
    
    <script src="js/main.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            checkLoginStatus();
            loadThreads();
            document.getElementById('logoutLink').addEventListener('click', logout);
        });
    </script>
</body>
</html>