<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>论坛系统 - 帖子详情</title>
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
        <div id="threadContainer">
            <!-- 帖子详情将通过JavaScript动态加载 -->
        </div>
        
        <div class="replies-container">
            <div class="replies-header">
                <h3>回复 (<span id="replyCount">0</span>)</h3>
            </div>
            
            <div id="repliesList">
                <!-- 回复列表将通过JavaScript动态加载 -->
            </div>
            
            <div class="reply-form">
                <h4>发表回复</h4>
                
                <div id="errorMessage" class="error-message"></div>
                
                <form id="replyForm">
                    <div class="form-group">
                        <label for="content">回复内容:</label>
                        <textarea id="content" name="content" required></textarea>
                    </div>
                    <div class="form-group">
                        <input type="submit" value="发表回复" class="btn">
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script src="js/main.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            checkLoginStatus();
            
            const urlParams = new URLSearchParams(window.location.search);
            const threadId = urlParams.get('id');
            
            if (threadId) {
                loadThread(threadId);
            } else {
                window.location.href = 'home.jsp?error=' + encodeURIComponent('帖子ID不能为空');
            }
            
            document.getElementById('replyForm').addEventListener('submit', function(e) {
                e.preventDefault();
                addReply(threadId);
            });
            
            document.getElementById('logoutLink').addEventListener('click', logout);
            
            const error = urlParams.get('error');
            if (error) {
                document.getElementById('errorMessage').textContent = decodeURIComponent(error);
            }
        });
    </script>
</body>
</html>