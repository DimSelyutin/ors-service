package com.innowise.swimdom.controller;

import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.JwtResponse;
import com.innowise.swimdom.dto.RefreshJwtRequest;
import com.innowise.swimdom.mapper.JwtResponseMapper;
import com.innowise.swimdom.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller for authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authenticationService;
    private final JwtResponseMapper jwtMapper;

    @PostMapping("/login")
    public Mono<JwtResponse> authenticateUser(@Valid @RequestBody AuthenticationRequestDto authRequest) {
        return authenticationService.login(authRequest)
            .map(jwtMapper::mapToDto);
    }

    @PostMapping("/refresh")
    public Mono<JwtResponse> getNewAccessToken(@Valid @RequestBody RefreshJwtRequest refreshJwtRequest) {
        return authenticationService.getNewAccessToken(refreshJwtRequest.refreshToken())
            .map(jwtMapper::mapToDto);
    }
}
