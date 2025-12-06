package com.forum.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Thread implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private List<Reply> replies;
    
    public Thread() {
        this.replies = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }
    
    public Thread(int id, String title, String content, String author) {
        this();
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
        }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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
    
    public List<Reply> getReplies() {
        return replies;
    }
    
    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
    
    public void addReply(Reply reply) {
        this.replies.add(reply);
    }
}