package com.innowise.swimdom.service;


import com.innowise.swimdom.entity.User;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    User updateUser(User user);

    void deleteUser(UUID id);
}


