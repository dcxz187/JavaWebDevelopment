package com.forum.servlet;

import com.forum.dao.DataStore;
import com.forum.model.Reply;
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

@WebServlet("/api/addReply")
public class AddReplyServlet extends HttpServlet {
    
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
            String threadIdParam = request.getParameter("threadId");
            String content = request.getParameter("content");
            
            // 检查参数
            if (threadIdParam == null || threadIdParam.isEmpty()) {
                result.put("success", false);
                result.put("message", "帖子ID不能为空");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }
            
            if (content == null || content.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "回复内容不能为空");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
                return;
            }
            
            try {
                int threadId = Integer.parseInt(threadIdParam);
                
                // 创建新回复
                Reply reply = new Reply();
                reply.setId(DataStore.generateReplyId());
                reply.setContent(content.trim());
                reply.setAuthor(user.getUsername());
                reply.setThreadId(threadId);
                
                // 保存回复
                DataStore.addReply(reply);
                
                result.put("success", true);
                result.put("message", "回复发表成功");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "无效的帖子ID");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发表回复时发生错误: " + e.getMessage());
            response.getWriter().write(new com.google.gson.Gson().toJson(result));
        }
    }
}