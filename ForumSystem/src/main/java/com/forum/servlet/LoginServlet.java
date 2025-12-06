package com.forum.servlet;

import com.forum.dao.DataStore;
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

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        
        // 获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String captcha = request.getParameter("captcha");
        
        // 创建响应结果
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证验证码
            HttpSession session = request.getSession();
            String sessionCaptcha = (String) session.getAttribute("captcha");
            
            // 移除session中的验证码，确保一次有效
            session.removeAttribute("captcha");
            
            // 检查验证码
            if (captcha == null || sessionCaptcha == null || !captcha.equalsIgnoreCase(sessionCaptcha)) {
                result.put("success", false);
                result.put("message", "验证码错误");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }
            
            // 验证用户名和密码
            User user = DataStore.getUserByUsername(username);
            
            if (user == null || !user.getPassword().equals(password)) {
                result.put("success", false);
                result.put("message", "用户名或密码错误");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }
            
            // 登录成功，将用户信息存储到session中
            session.setAttribute("user", user);

            result.put("success", true);
            result.put("message", "登录成功");
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录时发生错误: " + e.getMessage());
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        }
    }
}