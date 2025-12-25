package com.forum.service;

import com.forum.model.Thread;
import java.util.List;

public interface ThreadService {
    List<Thread> findAll();
    Thread findById(int id);
    void save(Thread thread);
    int generateId();
}