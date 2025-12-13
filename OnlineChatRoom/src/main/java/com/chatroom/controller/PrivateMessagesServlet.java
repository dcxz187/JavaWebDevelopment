package com.chatroom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@WebServlet("/api/private-messages")
public class PrivateMessagesServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
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
        if (recipient == null || recipient.trim().isEmpty()) {
            ApiResponse apiResponse = new ApiResponse(false, "接收者不能为空", null);
            String jsonResponse = gson.toJson(apiResponse);
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            return;
        }
        
        // 获取私聊消息
        List<ChatMessage> privateMessages = MessageStore.getPrivateMessages(username, recipient);
        
        // 构造返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("messages", privateMessages);
        data.put("currentUser", username);
        data.put("recipient", recipient);
        
        ApiResponse apiResponse = new ApiResponse(true, "获取私聊消息成功", data);
        String jsonResponse = gson.toJson(apiResponse);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}