package com.forum.servlet;

import com.forum.dao.DataStore;
import com.forum.model.Thread;
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

@WebServlet("/api/createThread")
public class CreateThreadServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
            
            // 获取请求参数
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            
            // 检查参数
            if (title == null || title.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "标题不能为空");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }
            
            if (content == null || content.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "内容不能为空");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }
            
            // 创建新帖子
            Thread thread = new Thread();
            thread.setId(DataStore.generateThreadId());
            thread.setTitle(title.trim());
            thread.setContent(content.trim());
            thread.setAuthor(user.getUsername());
            
            // 保存帖子
            DataStore.addThread(thread);
            
            result.put("success", true);
            result.put("message", "帖子创建成功");
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建帖子时发生错误: " + e.getMessage());
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        }
    }
}