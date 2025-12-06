package com.forum.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");

        // 创建响应结果
        Map<String, Object> result = new HashMap<>();

        try {
            // 销毁session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            result.put("success", true);
            result.put("message", "退出登录成功");
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "退出登录时发生错误: " + e.getMessage());
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        }
    }
}