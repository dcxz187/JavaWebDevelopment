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

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");

        // 获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String captcha = request.getParameter("captcha");

        // 创建响应结果
        Map<String, Object> result = new HashMap<>();

        try {
            // 检查参数是否完整
            if (username == null || password == null || confirmPassword == null || captcha == null) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }

            // 检查参数是否为空字符串
            if (username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty() || captcha.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }

            // 验证验证码
            HttpSession session = request.getSession();
            String sessionCaptcha = (String) session.getAttribute("captcha");

            // 移除session中的验证码，确保一次有效
            session.removeAttribute("captcha");

            // 检查验证码
            if (!captcha.equalsIgnoreCase(sessionCaptcha)) {
                result.put("success", false);
                result.put("message", "验证码错误");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }

            // 检查两次输入的密码是否一致
            if (!password.equals(confirmPassword)) {
                result.put("success", false);
                result.put("message", "两次输入的密码不一致");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }

            // 检查用户名是否已存在
            if (DataStore.isUsernameExists(username)) {
                result.put("success", false);
                result.put("message", "用户名已存在");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }

            // 创建新用户并保存
            User user = new User(username, password);
            DataStore.addUser(user);

            result.put("success", true);
            result.put("message", "注册成功");
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册时发生错误: " + e.getMessage());
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        }
    }
}