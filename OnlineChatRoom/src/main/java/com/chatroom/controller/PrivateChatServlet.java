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

@WebServlet("/private-chat")
public class PrivateChatServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/private_chat.html").forward(request, response);
    }
}