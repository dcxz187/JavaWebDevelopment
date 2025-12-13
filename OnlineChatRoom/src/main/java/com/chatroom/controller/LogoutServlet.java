package com.chatroom.controller;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
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