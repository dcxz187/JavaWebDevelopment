package com.secondhand.servlet;

import com.google.gson.Gson;
import com.secondhand.dao.ProductDAO;
import com.secondhand.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置请求编码
        request.setCharacterEncoding("UTF-8");
        
        // 获取搜索关键字
        String keyword = request.getParameter("keyword");
        
        // 查询物品列表
        List<Product> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productDAO.findByKeyword(keyword);
        } else {
            products = productDAO.findAll();
        }
        
        // 判断是API请求还是页面请求
        String xhr = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xhr)) {
            // API请求，返回JSON数据
            handleApiRequest(request, response, products);
        } else {
            // 页面请求，转发到JSP页面
            handlePageRequest(request, response, products, keyword);
        }
    }
    
    /**
     * 处理API请求
     */
    private void handleApiRequest(HttpServletRequest request, HttpServletResponse response, List<Product> products) 
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", products);
        
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(result));
    }
    
    /**
     * 处理页面请求
     */
    private void handlePageRequest(HttpServletRequest request, HttpServletResponse response, 
                                  List<Product> products, String keyword) 
            throws ServletException, IOException {
        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/jsp/product_list.jsp").forward(request, response);
    }
}