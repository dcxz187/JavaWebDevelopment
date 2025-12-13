package com.chatroom.filter;

import com.google.gson.Gson;
import com.chatroom.model.ApiResponse;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebFilter("/api/heartbeat")
public class HeartbeatFilter implements Filter {
    
    // 存储用户最后心跳时间
    public static final Map<String, Long> userLastHeartbeat = new ConcurrentHashMap<>();
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        
        if (username != null) {
            // 更新用户最后心跳时间
            userLastHeartbeat.put(username, System.currentTimeMillis());
            
            // 返回成功响应
            httpResponse.setContentType("application/json;charset=UTF-8");
            ApiResponse apiResponse = new ApiResponse(true, "Heartbeat received", null);
            
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(apiResponse);
            
            PrintWriter out = httpResponse.getWriter();
            out.print(jsonResponse);
            out.flush();
        } else {
            // 用户未登录
            httpResponse.setContentType("application/json;charset=UTF-8");
            ApiResponse apiResponse = new ApiResponse(false, "User not logged in", null);
            
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(apiResponse);
            
            PrintWriter out = httpResponse.getWriter();
            out.print(jsonResponse);
            out.flush();
        }
    }
}