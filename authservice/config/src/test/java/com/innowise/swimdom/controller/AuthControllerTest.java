package com.innowise.swimdom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.swimdom.exception.AuthenticationException;
import com.innowise.swimdom.openapi.model.AuthRequest;
import com.innowise.swimdom.openapi.model.AuthResponse;
import com.innowise.swimdom.openapi.model.JwtResponse;
import com.innowise.swimdom.openapi.model.RefreshJwtRequest;
import com.innowise.swimdom.openapi.model.UserCreateRequestDTO;
import com.innowise.swimdom.openapi.model.UserRole;
import com.innowise.swimdom.openapi.model.UserResponseDTO;
import com.innowise.swimdom.repository.UserRepository;
import com.innowise.swimdom.service.AuthenticationService;
import com.innowise.swimdom.service.UserService;
import com.innowise.swimdom.service.impl.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

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

        AuthResponse mockAuthResponse = new AuthResponse();
        mockAuthResponse.setToken("mocked-jwt-token");
        mockAuthResponse.setUser(new UserResponseDTO().email("user@example.com"));

        when(authenticationService.login(any(AuthRequest.class)))
            .thenReturn(mockAuthResponse);

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
        when(authenticationService.login(any(AuthRequest.class)))
            .thenThrow(new AuthenticationException("Invalid credentials"));

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
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("BAD REQUEST"));
    }

    @Test
    void registerUser_Success_ReturnsCreated() throws Exception {
        UserCreateRequestDTO registerRequest = new UserCreateRequestDTO();
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPhone("375291521111");
        registerRequest.setPassword("securePassword123!");
        registerRequest.setSurname("seassword123");
        registerRequest.setName("New User");
        registerRequest.setRole(UserRole.USER);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setEmail("newuser@example.com");
        userResponseDTO.setName("New User");
        userResponseDTO.setName("New User");
        userResponseDTO.setSurname("Surname");
        userResponseDTO.setId(UUID.randomUUID());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("mocked-jwt-token");
        authResponse.setUser(userResponseDTO);

        when(authenticationService.registerUser(any(UserCreateRequestDTO.class)))
            .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
            .andExpect(jsonPath("$.user.email").value("newuser@example.com"))
            .andExpect(jsonPath("$.user.name").value("New User"));
    }

    @Test
    void getNewAccessToken_Success_ReturnsOk() throws Exception {
        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest();
        refreshJwtRequest.setRefreshToken("mocked-refresh-token");

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken("new-mocked-access-token");
        jwtResponse.setRefreshToken("mocked-refresh-token");

        when(authenticationService.getNewAccessToken(any(RefreshJwtRequest.class)))
            .thenReturn(jwtResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshJwtRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("new-mocked-access-token"))
            .andExpect(jsonPath("$.refreshToken").value("mocked-refresh-token"));
    }
}