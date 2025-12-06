package com.chatroom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet({"/login", "/chat", "/api/messages", "/api/send", "/logout"})
public class ChatController extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final Gson gson = new Gson();
    
    // 用于存储所有聊天消息的静态列表
    private static final List<ChatMessage> messages = new ArrayList<>();

    // 聊天消息实体类
    public static class ChatMessage {
        private String username;
        private String content;
        private String timestamp;
        
        public ChatMessage() {}
        
        public ChatMessage(String username, String content) {
            this.username = username;
            this.content = content;
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }
    
    // API响应类
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/login":
                showLogin(request, response);
                break;
            case "/chat":
                showChat(request, response);
                break;
            case "/api/messages":
                getMessagesApi(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/login":
                processLogin(request, response);
                break;
            case "/api/send":
                sendMessageApi(request, response);
                break;
            case "/logout":
                processLogout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    private void showLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.html").forward(request, response);
    }
    
    private void processLogin(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String username = request.getParameter("username");
        
        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("login?error=empty");
            return;
        }
        
        // 将用户名保存到session中
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        
        // 添加用户到在线用户列表
        synchronized (getServletContext()) {
            @SuppressWarnings("unchecked")
            List<String> onlineUsers = (List<String>) getServletContext().getAttribute("onlineUsers");
            if (onlineUsers == null) {
                onlineUsers = new ArrayList<>();
                getServletContext().setAttribute("onlineUsers", onlineUsers);
            }
            
            if (!onlineUsers.contains(username)) {
                onlineUsers.add(username);
            }
        }
        
        // 重定向到聊天页面
        response.sendRedirect("chat");
    }
    
    private void showChat(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        if (username == null) {
            response.sendRedirect("login");
            return;
        }
        
        request.getRequestDispatcher("/chat.html").forward(request, response);
    }
    
    private void getMessagesApi(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
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
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("messages", messages);
        
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
    
    private void sendMessageApi(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
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
        synchronized (messages) {
            messages.add(message);
            // 限制消息数量，只保留最新的100条消息
            if (messages.size() > 100) {
                messages.remove(0);
            }
        }
        
        ApiResponse apiResponse = new ApiResponse(true, "发送成功", null);
        String jsonResponse = gson.toJson(apiResponse);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
    
    private void processLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
                // 从在线用户列表中移除用户
                @SuppressWarnings("unchecked")
                List<String> onlineUsers = (List<String>) getServletContext().getAttribute("onlineUsers");
                if (onlineUsers != null) {
                    synchronized (onlineUsers) {
                        onlineUsers.remove(username);
                    }
                }
            }
            session.invalidate();
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }
}