package com.chatroom.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
            
            if (!onlineUsers.contains(username)) {
                onlineUsers.add(username);
            }
        }
        
        // 重定向到聊天页面
        response.sendRedirect("chat");
    }
}