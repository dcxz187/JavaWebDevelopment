package com.forum.servlet;

import com.forum.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/user")
public class UserServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // 创建响应结果
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户未登录");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }
            
            // 返回用户信息
            Map<String, String> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            
            result.put("success", true);
            result.put("data", userData);
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户信息时发生错误: " + e.getMessage());
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        }
    }
}