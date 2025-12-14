package com.chatroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    // 消息类型: public(公共消息), private(私聊消息), system(系统消息)
    private String type;
    private String username;
    private String recipient; // 私聊接收者
    private String content;
    private String timestamp;
    
    public ChatMessage(String type, String username, String content) {
        this.type = type;
        this.username = username;
        this.content = content;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public ChatMessage(String type, String username, String recipient, String content) {
        this.type = type;
        this.username = username;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}