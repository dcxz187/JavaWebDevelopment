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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/deleteProduct")
public class DeleteProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
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
            
            // 获取物品ID参数
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "无效的物品ID");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            int productId = Integer.parseInt(idParam);
            
            // 查询物品详情
            Product product = productDAO.findById(productId);
            if (product == null) {
                result.put("success", false);
                result.put("message", "物品不存在");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 检查用户是否有权限删除该物品
            if (!product.getOwnerId().equals(user.getId())) {
                result.put("success", false);
                result.put("message", "您没有权限删除该物品");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 删除物品
            if (productDAO.deleteProduct(productId)) {
                result.put("success", true);
                result.put("message", "物品删除成功");
            } else {
                result.put("success", false);
                result.put("message", "物品删除失败，请稍后重试");
            }
            
            response.getWriter().write(new Gson().toJson(result));
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "无效的物品ID格式");
            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除物品时发生错误: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(result));
        }
    }
}