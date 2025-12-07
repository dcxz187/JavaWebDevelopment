<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>编辑物品 - 二手物品交易平台</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
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
            <h2>编辑物品</h2>
            
            <div id="errorMessage" class="error-message" style="display:none;"></div>
            <div id="successMessage" class="success-message" style="display:none;"></div>
            
            <form id="editProductForm">
                <input type="hidden" name="id" value="${product.id}">
                <div class="form-group">
                    <label for="name">物品名称:</label>
                    <input type="text" id="name" name="name" value="${product.name}" required>
                </div>
                
                <div class="form-group">
                    <label for="description">物品描述:</label>
                    <textarea id="description" name="description" required>${product.description}</textarea>
                </div>
                
                <div class="form-group">
                    <label for="price">价格:</label>
                    <input type="number" id="price" name="price" step="0.01" min="0" value="${product.price}" required>
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn">更新</button>
                    <a href="<%=request.getContextPath()%>/product?id=${product.id}" class="btn btn-cancel">取消</a>
                </div>
            </form>
        </div>
    </div>

    <script data-context-path="<%=request.getContextPath()%>" src="<%=request.getContextPath()%>/js/editProduct.js"></script>
</body>
</html>