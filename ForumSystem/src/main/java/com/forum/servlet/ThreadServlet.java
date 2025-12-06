package com.forum.servlet;

import com.forum.dao.DataStore;
import com.forum.model.Reply;
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

@WebServlet("/api/thread")
public class ThreadServlet extends HttpServlet {
    
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
            
            // 获取帖子ID参数
            String threadIdParam = request.getParameter("id");
            if (threadIdParam == null || threadIdParam.isEmpty()) {
                result.put("success", false);
                result.put("message", "帖子ID不能为空");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            try {
                int threadId = Integer.parseInt(threadIdParam);
                
                // 获取帖子信息
                Thread thread = DataStore.getThreadById(threadId);
                if (thread == null) {
                    result.put("success", false);
                    result.put("message", "帖子不存在");
                    response.getWriter().write(gson.toJson(result));
                    return;
                }
                
                // 获取帖子的回复列表
                List<Reply> replies = DataStore.getRepliesByThreadId(threadId);
                
                // 构造返回数据
                Map<String, Object> data = new HashMap<>();
                data.put("thread", thread);
                data.put("replies", replies);
                
                result.put("success", true);
                result.put("data", data);
                response.getWriter().write(gson.toJson(result));
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "无效的帖子ID");
                response.getWriter().write(gson.toJson(result));
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取帖子详情时发生错误: " + e.getMessage());
            response.getWriter().write(gson.toJson(result));
        }
    }
}