package com.forum.service;

import com.forum.model.User;

public interface UserService {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    void save(User user);
}