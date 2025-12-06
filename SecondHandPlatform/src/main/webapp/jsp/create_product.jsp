<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>发布物品 - 二手物品交易平台</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>二手物品交易平台</h1>
            <nav>
                <a href="<%=request.getContextPath()%>/products">首页</a>
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <a href="<%=request.getContextPath()%>/jsp/create_product.jsp">发布物品</a>
                        <span>欢迎, ${sessionScope.user.username}!</span>
                        <a href="<%=request.getContextPath()%>/logout">退出</a>
                    </c:when>
                    <c:otherwise>
                        <a href="<%=request.getContextPath()%>/jsp/login.jsp">登录</a>
                        <a href="<%=request.getContextPath()%>/jsp/register.jsp">注册</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </header>
        
        <div class="form-container">
            <h2>发布物品</h2>
            
            <div id="errorMessage" class="error-message" style="display:none;"></div>
            <div id="successMessage" class="success-message" style="display:none;"></div>
            
            <form id="createProductForm">
                <div class="form-group">
                    <label for="name">物品名称:</label>
                    <input type="text" id="name" name="name" required>
                </div>
                
                <div class="form-group">
                    <label for="description">物品描述:</label>
                    <textarea id="description" name="description" required></textarea>
                </div>
                
                <div class="form-group">
                    <label for="price">价格:</label>
                    <input type="number" id="price" name="price" step="0.01" min="0" required>
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn">发布</button>
                    <a href="<%=request.getContextPath()%>/products" class="btn btn-cancel">取消</a>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        document.getElementById('createProductForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            // 隐藏之前的消息
            document.getElementById('errorMessage').style.display = 'none';
            document.getElementById('successMessage').style.display = 'none';
            
            // 获取表单数据
            const formData = new FormData(this);
            
            // 发送发布物品请求
            fetch('<%=request.getContextPath()%>/createProduct', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 显示成功消息
                    document.getElementById('successMessage').textContent = data.message;
                    document.getElementById('successMessage').style.display = 'block';
                    
                    // 清空表单
                    document.getElementById('createProductForm').reset();
                    
                    // 2秒后跳转到首页
                    setTimeout(() => {
                        window.location.href = '<%=request.getContextPath()%>/products';
                    }, 2000);
                } else {
                    // 显示错误消息
                    document.getElementById('errorMessage').textContent = data.message;
                    document.getElementById('errorMessage').style.display = 'block';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('errorMessage').textContent = '发布物品时发生错误，请稍后重试';
                document.getElementById('errorMessage').style.display = 'block';
            });
        });
    </script>
</body>
</html>