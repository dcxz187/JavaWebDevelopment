<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>二手物品交易平台</title>
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
            <div class="search-section">
                <form action="<%=request.getContextPath()%>/products" method="get">
                    <input type="text" name="keyword" value="${keyword}" placeholder="搜索物品...">
                    <button type="submit">搜索</button>
                </form>
            </div>
            
            <div class="product-list">
                <h2>物品列表</h2>
                <c:forEach var="product" items="${products}">
                    <div class="product-item">
                        <h3><a href="<%=request.getContextPath()%>/product?id=${product.id}">${product.name}</a></h3>
                        <p>${product.description}</p>
                        <div class="product-meta">
                            <span class="price">￥${product.price}</span>
                            <span class="owner">发布者: ${product.owner.username}</span>
                            <span class="date">发布时间: ${product.createdAt}</span>
                        </div>
                    </div>
                </c:forEach>
                
                <c:if test="${empty products}">
                    <p>暂无物品信息。</p>
                </c:if>
            </div>
        </main>
    </div>
</body>
</html>