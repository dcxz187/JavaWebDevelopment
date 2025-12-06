package com.secondhand.servlet;

import com.google.gson.Gson;
import com.secondhand.dao.UserDAO;
import com.secondhand.model.User;
import com.secondhand.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 检查是否有成功消息（来自注册页面）
        String success = request.getParameter("success");
        if (success != null) {
            request.setAttribute("successMessage", success);
        }
        
        // 转发到登录页面
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        // 获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 创建响应结果
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查参数是否完整
            if (username == null || password == null) {
                result.put("success", false);
                result.put("message", "请填写用户名和密码");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 检查参数是否为空字符串
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "请填写用户名和密码");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 验证用户名和密码
            User user = userDAO.findByUsername(username);
            
            if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
                result.put("success", false);
                result.put("message", "用户名或密码错误");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 登录成功，将用户信息存储到session中
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            result.put("success", true);
            result.put("message", "登录成功");
            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录时发生错误: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(result));
        }
    }
}