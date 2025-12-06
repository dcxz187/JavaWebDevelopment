<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${product.name} - 二手物品交易平台</title>
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
        
        <main>
            <div class="product-detail">
                <h2>${product.name}</h2>
                <div class="product-info">
                    <p class="price">价格: ￥${product.price}</p>
                    <p class="description">${product.description}</p>
                    <div class="product-meta">
                        <p>发布者: ${product.owner.username}</p>
                        <p>发布时间: ${product.createdAt}</p>
                    </div>
                    
                    <c:if test="${isOwner}">
                        <div class="actions">
                            <a href="<%=request.getContextPath()%>/editProduct?id=${product.id}" class="btn">编辑</a>
                            <button id="deleteBtn" class="btn btn-cancel">删除</button>
                        </div>
                    </c:if>
                </div>
            </div>
        </main>
    </div>
    
    <script>
        <c:if test="${isOwner}">
        document.getElementById('deleteBtn').addEventListener('click', function() {
            if (confirm('确定要删除这个物品吗？')) {
                // 发送删除请求
                fetch('<%=request.getContextPath()%>/deleteProduct?id=${product.id}', {
                    method: 'POST'
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('物品删除成功');
                        window.location.href = '<%=request.getContextPath()%>/products';
                    } else {
                        alert('删除失败: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('删除物品时发生错误，请稍后重试');
                });
            }
        });
        </c:if>
    </script>
</body>
</html>