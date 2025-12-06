package com.forum.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Reply implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int threadId;
    
    public Reply() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Reply(int id, String content, String author, int threadId) {
        this();
        this.id = id;
        this.content = content;
        this.author = author;
        this.threadId = threadId;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public int getThreadId() {
        return threadId;
    }
    
    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}