package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.AuthRequest;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.JwtResponse;
import com.innowise.swimdom.openapi.model.RefreshJwtRequest;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import org.springframework.stereotype.Service;

/**
 * Interface for authentication.
 */
@Service
public interface AuthenticationService {

    AuthResponse login(AuthRequest loginRequest);

    AuthResponse registerUser(UserCreateRequestDTO signupRequest);

    JwtResponse getNewAccessToken(RefreshJwtRequest refreshJwtRequest);
}
