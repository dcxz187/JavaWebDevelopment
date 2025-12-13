package com.chatroom.model;

import java.util.ArrayList;
import java.util.List;

public class MessageStore {
    // 用于存储所有聊天消息的静态列表
    private static final List<ChatMessage> messages = new ArrayList<>();
    
    // 提供对消息列表的访问方法
    public static List<ChatMessage> getMessages() {
        return messages;
    }
}