package com.chatroom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

@WebServlet("/api/messages")
public class MessagesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
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
        
        // 构造返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("messages", MessageStore.getMessages());
        
        @SuppressWarnings("unchecked")
        List<String> onlineUsers = (List<String>) getServletContext().getAttribute("onlineUsers");
        if (onlineUsers == null) {
            onlineUsers = new ArrayList<>();
        }
        data.put("onlineUsers", onlineUsers);
        data.put("currentUser", username);
        
        ApiResponse apiResponse = new ApiResponse(true, "获取消息成功", data);
        String jsonResponse = gson.toJson(apiResponse);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}