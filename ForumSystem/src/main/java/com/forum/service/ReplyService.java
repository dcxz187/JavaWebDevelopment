package com.forum.service;

import com.forum.model.Reply;
import java.util.List;

public interface ReplyService {
    List<Reply> findByThreadId(int threadId);
    void save(Reply reply);
    int generateId();
}