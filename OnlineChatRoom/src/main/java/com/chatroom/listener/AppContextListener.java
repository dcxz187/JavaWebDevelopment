package com.chatroom.listener;

import com.chatroom.filter.HeartbeatFilter;
import com.chatroom.model.ChatMessage;
import com.chatroom.model.MessageStore;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AppContextListener implements ServletContextListener {
    
    private ScheduledExecutorService scheduler;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        
        // 初始化在线用户列表
        context.setAttribute("onlineUsers", new ArrayList<String>());
        
        // 启动后台任务，定期检查用户心跳
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new UserStatusChecker(context), 0, 30, TimeUnit.SECONDS);
        
        System.out.println("Application context initialized");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        System.out.println("Application context destroyed");
    }
    
    // 用户状态检查器
    private static class UserStatusChecker implements Runnable {
        private final ServletContext context;
        private static final long HEARTBEAT_TIMEOUT = 45 * 1000; // 45秒超时
        
        public UserStatusChecker(ServletContext context) {
            this.context = context;
        }
        
        @Override
        public void run() {
            try {
                @SuppressWarnings("unchecked")
                List<String> onlineUsers = (List<String>) context.getAttribute("onlineUsers");
                if (onlineUsers == null) {
                    return;
                }
                
                long currentTime = System.currentTimeMillis();
                
                // 检查每个用户的最后心跳时间
                synchronized (onlineUsers) {
                    onlineUsers.removeIf(username -> {
                        Long lastHeartbeat = HeartbeatFilter.userLastHeartbeat.get(username);
                        if (lastHeartbeat == null || currentTime - lastHeartbeat > HEARTBEAT_TIMEOUT) {
                            // 用户超时，认为已离线
                            HeartbeatFilter.userLastHeartbeat.remove(username);
                            
                            // 发送系统消息通知其他用户
                            ChatMessage systemMessage = new ChatMessage(
                                "system", "System", username + " 离开了聊天室（超时）");
                            synchronized (MessageStore.getMessages()) {
                                MessageStore.getMessages().add(systemMessage);
                                // 限制消息数量
                                if (MessageStore.getMessages().size() > 100) {
                                    MessageStore.getMessages().remove(0);
                                }
                            }
                            
                            System.out.println("User timed out and removed: " + username);
                            return true;
                        }
                        return false;
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}