package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.enums.UserRole;
import com.innowise.swimdom.exception.UserAlreadyExistsException;
import com.innowise.swimdom.exception.UserNotFoundException;
import com.innowise.swimdom.mapper.AuthMapper;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.openapi.model.UserUpdateRequestDTO;
import com.innowise.swimdom.repository.UserRepository;
import com.innowise.swimdom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public void deleteUser(UUID id) {
        log.debug("deleteUser - start: {}", id);
        userRepository.deleteById(id);
        log.debug("deleteUser - end: {}", id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User updateUser(UserUpdateRequestDTO userUpdateRequestDTO) {
        UUID userId = userUpdateRequestDTO.getId();
        log.debug("updateUser - Attempting to update user with ID: {}", userId);

        User existingUser = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("updateUser - User with ID {} not found.", userId);
                return new UserNotFoundException("User with ID " + userId + " not found.");
            });

        if (userUpdateRequestDTO.getEmail() != null
            && !userUpdateRequestDTO.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.findUserByEmail(userUpdateRequestDTO.getEmail()).isPresent()) {
                log.warn("updateUser - User update failed: Email {} is already taken.",
                    userUpdateRequestDTO.getEmail());
                throw new UserAlreadyExistsException(
                    "Email '" + userUpdateRequestDTO.getEmail() + "' is already taken.");
            }
            existingUser.setEmail(userUpdateRequestDTO.getEmail());
        }
        authMapper.updateUserFromDto(userUpdateRequestDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);
        log.debug("updateUser - User updated successfully with ID: {}", userId);

        return updatedUser;
    }

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
