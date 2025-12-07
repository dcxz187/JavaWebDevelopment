package com.secondhand.servlet;

import com.google.gson.Gson;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/createProduct")
public class CreateProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // 未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // 已登录，转发到发布物品页面
        request.getRequestDispatcher("/jsp/create_product.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // 创建响应结果
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (user == null) {
                result.put("success", false);
                result.put("message", "请先登录");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 获取请求参数
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            
            // 检查参数是否完整
            if (name == null || description == null || priceStr == null) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 检查参数是否为空字符串
            if (name.trim().isEmpty() || description.trim().isEmpty() || priceStr.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 验证价格格式
            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    result.put("success", false);
                    result.put("message", "价格必须大于0");
                    response.getWriter().write(new Gson().toJson(result));
                    return;
                }
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "价格格式不正确");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 创建新物品
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setOwnerId(user.getId());
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            // 保存到数据库
            if (productDAO.createProduct(product)) {
                result.put("success", true);
                result.put("message", "物品发布成功");
            } else {
                result.put("success", false);
                result.put("message", "物品发布失败，请稍后重试");
            }
            
            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发布物品时发生错误: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(result));
        }
    }
}