package com.chatroom.controller;

import java.io.IOException;
import java.io.PrintWriter;

import com.chatroom.model.MessageStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.chatroom.model.ApiResponse;
import com.chatroom.model.ChatMessage;

@WebServlet("/api/send")
public class SendMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final Gson gson = new Gson();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        if (username == null) {
            ApiResponse apiResponse = new ApiResponse(false, "用户未登录", null);
            String jsonResponse = gson.toJson(apiResponse);
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            return;
        }
        
        String content = request.getParameter("content");
        if (content == null || content.trim().isEmpty()) {
            ApiResponse apiResponse = new ApiResponse(false, "消息内容不能为空", null);
            String jsonResponse = gson.toJson(apiResponse);
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            return;
        }
        
        // 创建消息对象并添加到消息列表
        ChatMessage message = new ChatMessage(username, content);
        synchronized (MessageStore.getMessages()) {
            MessageStore.getMessages().add(message);
            // 限制消息数量，只保留最新的100条消息
            if (MessageStore.getMessages().size() > 100) {
                MessageStore.getMessages().remove(0);
            }
        }
        
        ApiResponse apiResponse = new ApiResponse(true, "发送成功", null);
        String jsonResponse = gson.toJson(apiResponse);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}