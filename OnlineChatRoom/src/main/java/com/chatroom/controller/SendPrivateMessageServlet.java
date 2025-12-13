package com.chatroom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;

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

@WebServlet("/api/send-private")
public class SendPrivateMessageServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final Gson gson = new Gson();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        
        if (username == null) {
            ApiResponse apiResponse = new ApiResponse(false, "用户未登录", null);
            String jsonResponse = gson.toJson(apiResponse);
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            return;
        }
        
        String recipient = request.getParameter("recipient");
        String content = request.getParameter("content");
        
        if (recipient == null || recipient.trim().isEmpty()) {
            ApiResponse apiResponse = new ApiResponse(false, "接收者不能为空", null);
            String jsonResponse = gson.toJson(apiResponse);
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            return;
        }
        
        if (content == null || content.trim().isEmpty()) {
            ApiResponse apiResponse = new ApiResponse(false, "消息内容不能为空", null);
            String jsonResponse = gson.toJson(apiResponse);
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            return;
        }
        
        // 创建私聊消息对象并添加到私聊消息列表
        ChatMessage message = new ChatMessage("private", username, recipient, content);
        synchronized (MessageStore.getPrivateMessages(username, recipient)) {
            MessageStore.addPrivateMessage(message);
        }
        
        ApiResponse apiResponse = new ApiResponse(true, "发送成功", null);
        String jsonResponse = gson.toJson(apiResponse);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}