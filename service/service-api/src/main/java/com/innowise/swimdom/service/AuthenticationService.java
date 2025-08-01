package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.AuthRequest;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import org.springframework.stereotype.Service;

/**
 * Interface for authentication.
 */
@Service
public interface AuthenticationService {

    AuthResponse login(AuthRequest loginRequest);

    AuthResponse registerUser(UserCreateRequestDTO signupRequest);
}
