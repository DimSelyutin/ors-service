package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.api.ApiApi;
import com.innowise.swimdom.openapi.model.AuthRequest;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements ApiApi {

    private final AuthenticationService authenticationService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        log.info("authenticateUser - start, authRequest: {}", authRequest.getEmail());
        ResponseEntity<AuthResponse> responseDto =
            ResponseEntity.ok(authenticationService.login(authRequest));
        log.info("authenticateUser - end, authRequest: {}", responseDto);
        return responseDto;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserCreateRequestDTO registerRequest) {
        log.info("registerUser - start, authRequest: {}", registerRequest.getEmail());
        ResponseEntity<AuthResponse> responseDto =
            ResponseEntity.ok(authenticationService.registerUser(registerRequest));
        log.info("registerUser - end, authRequest: {}", responseDto);
        return responseDto;
    }
}
