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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 转发到注册页面
        request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
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
        String confirmPassword = request.getParameter("confirmPassword");
        
        // 创建响应结果
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查参数是否完整
            if (username == null || password == null || confirmPassword == null) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 检查参数是否为空字符串
            if (username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 检查密码是否一致
            if (!password.equals(confirmPassword)) {
                result.put("success", false);
                result.put("message", "两次输入的密码不一致");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 检查用户名是否已存在
            if (userDAO.isUsernameExists(username)) {
                result.put("success", false);
                result.put("message", "用户名已存在");
                response.getWriter().write(new Gson().toJson(result));
                return;
            }
            
            // 创建新用户
            User user = new User();
            user.setUsername(username);
            user.setPassword(PasswordUtil.hashPassword(password));
            
            // 保存到数据库
            if (userDAO.createUser(user)) {
                result.put("success", true);
                result.put("message", "注册成功");
            } else {
                result.put("success", false);
                result.put("message", "注册失败，请稍后重试");
            }
            
            response.getWriter().write(new Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册时发生错误: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(result));
        }
    }
}