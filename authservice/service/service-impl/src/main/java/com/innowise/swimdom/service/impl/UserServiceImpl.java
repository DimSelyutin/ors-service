package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.enums.UserRole;
import com.innowise.swimdom.exception.UserAlreadyExistsException;
import com.innowise.swimdom.mapper.AuthMapper;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.repository.UserRepository;
import com.innowise.swimdom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A service for managing user subscriptions (UserSubscription).
 * Provides methods for creating, receiving, updating, and deleting user subscriptions.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User createUser(UserCreateRequestDTO userCreateRequestDTO) {
        log.debug("createUser - Attempting to create a new user with email: {}", userCreateRequestDTO.getEmail());

        if (userRepository.findUserByEmail(userCreateRequestDTO.getEmail()).isPresent()) {
            log.warn("createUser - User creation failed: User with email {} already exists.",
                userCreateRequestDTO.getEmail());
            throw new UserAlreadyExistsException(
                "User with email '" + userCreateRequestDTO.getEmail() + "' already exists."
            );
        }
        User newUser = authMapper.toUser(userCreateRequestDTO);
        newUser.setRole(UserRole.USER);
        newUser.setPassword(passwordEncoder.encode(userCreateRequestDTO.getPassword()));
        log.debug("createUser - Password encoded for user: {}", newUser);

        User savedUser = userRepository.save(newUser);
        log.debug("createUser - User created successfully with ID: {}", savedUser);

        return savedUser;
    }
}
