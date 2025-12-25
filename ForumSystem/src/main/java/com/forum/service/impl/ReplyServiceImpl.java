package com.forum.service.impl;

import com.forum.dao.DataStore;
import com.forum.model.Reply;
import com.forum.service.ReplyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Override
    public List<Reply> findByThreadId(int threadId) {
        return DataStore.getRepliesByThreadId(threadId);
    }

    @Override
    public void save(Reply reply) {
        DataStore.addReply(reply);
    }

    @Override
    public int generateId() {
        return DataStore.generateReplyId();
    }
}