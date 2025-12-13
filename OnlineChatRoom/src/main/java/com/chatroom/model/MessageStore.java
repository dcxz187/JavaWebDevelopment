package com.chatroom.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageStore {
    // 提供对消息列表的访问方法
    // 用于存储所有聊天消息的静态列表
    @Getter
    private static final List<ChatMessage> messages = new ArrayList<>();
    
    // 用于存储私聊消息的映射，键为"用户A_用户B"格式的字符串
    private static final Map<String, List<ChatMessage>> privateMessages = new ConcurrentHashMap<>();

    // 获取私聊消息
    public static List<ChatMessage> getPrivateMessages(String user1, String user2) {
        String key = generatePrivateChatKey(user1, user2);
        return privateMessages.computeIfAbsent(key, k -> new ArrayList<>());
    }
    
    // 添加私聊消息
    public static void addPrivateMessage(ChatMessage message) {
        String key = generatePrivateChatKey(message.getUsername(), message.getRecipient());
        privateMessages.computeIfAbsent(key, k -> new ArrayList<>()).add(message);
        
        // 限制私聊消息数量
        List<ChatMessage> chatMessages = privateMessages.get(key);
        if (chatMessages.size() > 50) {
            chatMessages.remove(0);
        }
    }
    
    // 生成私聊标识键
    private static String generatePrivateChatKey(String user1, String user2) {
        // 保证两个用户名按字典序排列，这样无论谁是发送方都能得到相同的键
        return user1.compareTo(user2) <= 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }
}