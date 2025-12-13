package com.chatroom.filter;

import com.chatroom.model.ApiResponse;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // 静态资源和公开页面不需要身份验证
        if (uri.endsWith(".css") || uri.endsWith(".js") || 
            uri.endsWith(".png") || uri.endsWith(".jpg") || uri.endsWith(".jpeg") ||
            uri.equals(contextPath + "/") || 
            uri.equals(contextPath + "/login") || 
            uri.equals(contextPath + "/register")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 获取会话
        HttpSession session = httpRequest.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        
        // 如果用户未登录且访问的是受保护资源
        if (username == null && 
            (uri.startsWith(contextPath + "/chat") || 
             uri.startsWith(contextPath + "/api/"))) {
            
            // 对API请求返回JSON错误而不是重定向
            if (uri.startsWith(contextPath + "/api/")) {
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                
                ApiResponse apiResponse = new ApiResponse(false, "用户未登录", null);
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(apiResponse);
                
                PrintWriter out = httpResponse.getWriter();
                out.print(jsonResponse);
                out.flush();
                return;
            } else {
                // 对页面请求重定向到登录页面
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            }
        }
        
        // 继续处理请求
        chain.doFilter(request, response);
    }
}