package com.chatroom.util;

import jakarta.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话管理器，用于维护用户与会话之间的映射关系
 */
public class SessionManager {
    // 用户名到会话列表的映射
    private static final Map<String, List<HttpSession>> userSessions = new ConcurrentHashMap<>();
    
    /**
     * 添加用户会话
     * @param username 用户名
     * @param session 会话
     */
    public static void addUserSession(String username, HttpSession session) {
        userSessions.computeIfAbsent(username, k -> new ArrayList<>()).add(session);
    }
    
    /**
     * 移除用户的所有会话
     * @param username 用户名
     */
    public static void removeUserSessions(String username) {
        List<HttpSession> sessions = userSessions.remove(username);
        if (sessions != null) {
            for (HttpSession session : sessions) {
                try {
                    session.invalidate();
                } catch (IllegalStateException e) {
                    // 会话已经失效，忽略异常
                }
            }
        }
    }
    
    /**
     * 移除特定会话
     * @param username 用户名
     * @param session 会话
     */
    public static void removeUserSession(String username, HttpSession session) {
        List<HttpSession> sessions = userSessions.get(username);
        if (sessions != null) {
            sessions.remove(session);
            // 如果用户没有更多会话，从映射中移除用户
            if (sessions.isEmpty()) {
                userSessions.remove(username);
            }
        }
    }
    
    /**
     * 获取用户的会话数量
     * @param username 用户名
     * @return 会话数量
     */
    public static int getUserSessionCount(String username) {
        List<HttpSession> sessions = userSessions.get(username);
        return sessions != null ? sessions.size() : 0;
    }
}