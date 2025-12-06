package com.forum.servlet;

import com.forum.dao.DataStore;
import com.forum.model.Thread;
import com.forum.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/threads")
public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // 创建Gson实例，用于处理LocalDateTime序列化
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
                    return new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                })
                .create();
        
        // 创建响应结果
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户未登录");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            // 获取所有帖子
            List<Thread> threads = DataStore.getAllThreads();
            
            result.put("success", true);
            result.put("data", threads);
            response.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取帖子列表时发生错误: " + e.getMessage());
            response.getWriter().write(gson.toJson(result));
        }
    }
}