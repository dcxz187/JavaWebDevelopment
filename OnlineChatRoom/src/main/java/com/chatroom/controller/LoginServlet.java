package com.chatroom.controller;

import java.io.IOException;

import com.chatroom.model.ChatMessage;
import com.chatroom.model.MessageStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.html").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
            
            // 检查用户是否首次登录
            boolean isFirstLogin = !onlineUsers.contains(username);
            
            if (isFirstLogin) {
                onlineUsers.add(username);
                
                // 发送系统消息通知所有用户有新用户加入
                ChatMessage systemMessage = new ChatMessage("system", "System", username + " 加入了聊天室");
                synchronized (MessageStore.getMessages()) {
                    MessageStore.getMessages().add(systemMessage);
                    // 限制消息数量
                    if (MessageStore.getMessages().size() > 100) {
                        MessageStore.getMessages().remove(0);
                    }
                }
            }
        }
        
        // 重定向到聊天页面
        response.sendRedirect("chat");
    }
}