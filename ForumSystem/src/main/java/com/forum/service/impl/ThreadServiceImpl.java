package com.forum.service.impl;

import com.forum.dao.DataStore;
import com.forum.model.Thread;
import com.forum.service.ThreadService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadServiceImpl implements ThreadService {

    @Override
    public List<Thread> findAll() {
        return DataStore.getAllThreads();
    }

    @Override
    public Thread findById(int id) {
        return DataStore.getThreadById(id);
    }

    @Override
    public void save(Thread thread) {
        DataStore.addThread(thread);
    }

    @Override
    public int generateId() {
        return DataStore.generateThreadId();
    }
}