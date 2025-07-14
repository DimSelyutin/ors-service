package com.innowise.swimdom.service;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;

import java.util.UUID;

/**
 * A service for managing users.
 * Provides methods for creating, receiving, updating, and deleting user.
 */
public interface UserService {

    User createUser(UserCreateRequestDTO userCreateRequestDTO);

    void deleteUser(UUID id);

    User updateUser();
}
