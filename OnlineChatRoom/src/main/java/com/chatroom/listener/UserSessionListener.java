package com.chatroom.listener;

import com.chatroom.model.ChatMessage;
import com.chatroom.model.MessageStore;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.ArrayList;
import java.util.List;

@WebListener
public class UserSessionListener implements HttpSessionListener {
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 会话创建时不需要特别处理
        System.out.println("Session created: " + se.getSession().getId());
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session destroyed: " + se.getSession().getId());
        
        // 获取ServletContext
        ServletContext context = se.getSession().getServletContext();
        
        // 获取已登录用户名
        String username = (String) se.getSession().getAttribute("username");
        if (username != null) {
            System.out.println("User logged out: " + username);
            
            // 从在线用户列表中移除用户
            @SuppressWarnings("unchecked")
            List<String> onlineUsers = (List<String>) context.getAttribute("onlineUsers");
            if (onlineUsers != null) {
                synchronized (onlineUsers) {
                    onlineUsers.remove(username);
                }
            }
            
            // 创建系统消息通知其他用户
            ChatMessage systemMessage = new ChatMessage("system", "System", username + " 离开了聊天室");
            synchronized (MessageStore.getMessages()) {
                MessageStore.getMessages().add(systemMessage);
                // 限制消息数量
                if (MessageStore.getMessages().size() > 100) {
                    MessageStore.getMessages().remove(0);
                }
            }
            
            System.out.println("Broadcasted logout message for user: " + username);
        }
    }
}