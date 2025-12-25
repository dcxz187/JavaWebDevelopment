package com.forum.service.impl;

import com.forum.dao.DataStore;
import com.forum.model.User;
import com.forum.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findByUsername(String username) {
        return DataStore.getUserByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return DataStore.isUsernameExists(username);
    }

    @Override
    public void save(User user) {
        DataStore.addUser(user);
    }
}