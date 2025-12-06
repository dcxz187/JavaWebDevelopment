package com.secondhand.servlet;

import com.secondhand.dao.ProductDAO;
import com.secondhand.model.Product;
import com.secondhand.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 获取物品ID参数
        String idParam = request.getParameter("id");
        
        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                int productId = Integer.parseInt(idParam);
                
                // 查询物品详情
                Product product = productDAO.findById(productId);
                
                if (product != null) {
                    request.setAttribute("product", product);
                    
                    // 检查当前用户是否为物品所有者
                    HttpSession session = request.getSession();
                    User currentUser = (User) session.getAttribute("user");
                    if (currentUser != null && currentUser.getId().equals(product.getOwnerId())) {
                        request.setAttribute("isOwner", true);
                    }
                    
                    // 转发到物品详情页面
                    request.getRequestDispatcher("/jsp/product_detail.jsp").forward(request, response);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            // ID格式不正确
        }
        
        // 物品不存在或ID无效，重定向到首页
        response.sendRedirect(request.getContextPath() + "/products");
    }
}