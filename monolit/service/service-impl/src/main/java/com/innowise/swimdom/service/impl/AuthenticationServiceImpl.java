package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.exception.AuthenticationException;
import com.innowise.swimdom.mapper.AuthMapper;
import com.innowise.swimdom.openapi.model.AuthRequest;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.service.AuthenticationService;
import com.innowise.swimdom.service.UserService;
import com.innowise.swimdom.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider tokenProvider;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    /**
     * Method for login user.
     *
     * @param loginRequest request with email and password.
     * @return AuthResponse.
     */
    @Override
    public AuthResponse login(AuthRequest loginRequest) {
        log.debug("Login - start", loginRequest);
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            User principal = (User) authentication.getPrincipal();

            return authMapper.toAuthResponse(jwt, principal);
        } catch (BadCredentialsException | AuthenticationException ex) {
            throw new AuthenticationException(Constants.INVALID_CREDENTIAL);
        }
    }

    /**
     * Method for registering user.
     *
     * @param signupRequest request with email and password.
     * @return UserResponseDTO
     */
    @Override
    @Transactional
    public AuthResponse registerUser(UserCreateRequestDTO signupRequest) {
        log.debug("register user - start, {}", signupRequest.getEmail());
        User registeredUser = userService.createUser(signupRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(registeredUser.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        log.debug("register user - start, {}", registeredUser.getEmail());
        return authMapper.toAuthResponse(jwt, registeredUser);
    }
}
