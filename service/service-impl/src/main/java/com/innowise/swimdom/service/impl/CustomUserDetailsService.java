package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * Custom service for UserDetails.
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User with email: {} not found!" + email));
        return build(user);
    }

    public User loadUserById(UUID id) {
        return userRepository.findUserById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id"));
    }

    public static User build(User user) {
        Set<GrantedAuthority> authorities =
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return user;
    }
}
