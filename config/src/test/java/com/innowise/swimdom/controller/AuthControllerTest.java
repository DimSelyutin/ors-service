package com.innowise.swimdom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.swimdom.openapi.model.AuthRequest;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.repository.UserRepository;
import com.innowise.swimdom.service.impl.JwtTokenProvider;
import com.innowise.swimdom.service.AuthenticationService;
import com.innowise.swimdom.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationService authenticationService;

    private String expectedAuthResponseJson;

    @BeforeEach
    void setUp() throws Exception {
        expectedAuthResponseJson = Files.readString(Path.of("src/test/resources/expected-auth-response.json"));
    }

    @Test
    void authenticateUser_Success() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@example.com");
        authRequest.setPassword("password");

        Authentication authentication = Mockito.mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);

        when(tokenProvider.generateToken(authentication))
            .thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedAuthResponseJson));
    }

    @Test
    void authenticateUser_InvalidPassword_ReturnsUnauthorized() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@example.com");
        authRequest.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errorMessage").value(
                "UNAUTHORIZED"));
    }

    @Test
    void registerUser_InvalidInput_ReturnsBadRequest() throws Exception {
        UserCreateRequestDTO registerRequest = new UserCreateRequestDTO();
        registerRequest.setEmail("invalid-email");
        registerRequest.setPassword("short");
        registerRequest.setName("");
        mockMvc.perform(post("/api/v1/auth/register") // Убедитесь, что путь "/api/v1/auth/register" корректен
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest()) // Ожидаем 400 Bad Request
            .andExpect(jsonPath("$.errorMessage").value("BAD REQUEST"));
    }
}
