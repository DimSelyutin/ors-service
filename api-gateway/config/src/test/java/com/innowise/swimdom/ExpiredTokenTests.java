package com.innowise.swimdom;

import static com.innowise.swimdom.util.TestConstants.AUTHENTICATION_URI;
import static com.innowise.swimdom.util.TestConstants.CLIENTS_URI;
import static com.innowise.swimdom.util.TestConstants.REFRESH_URI;
import static com.innowise.swimdom.util.TestConstants.VALID_JWT_REQUEST;
import static com.innowise.swimdom.util.TestConstants.USER_INFO_RESPONSE_DTO;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.innowise.swimdom.dto.JwtResponse;
import com.innowise.swimdom.dto.RefreshJwtRequest;
import com.innowise.swimdom.util.TestUtils;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import com.innowise.swimdom.service.UserServiceClient;
import com.innowise.swimdom.service.UserServiceFallbackFactory;
import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.UserEmailRequestDto;
import reactor.core.publisher.Mono;

/**
 * Integration tests for expired tokens.
 */
@SpringBootTest(properties = { 
    "jwt.accessExpirationMinutes=-1", 
    "jwt.refreshExpirationDays=-1",
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false",
    "spring.main.allow-bean-definition-overriding=true"
})
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
@EnableReactiveFeignClients
public class ExpiredTokenTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @MockBean
    private ReactiveValueOperations<String, String> reactiveValueOperations;

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private UserServiceFallbackFactory userServiceFallbackFactory;

    @BeforeEach
    void mockRedis() {
        Mockito.when(reactiveRedisTemplate.opsForValue()).thenReturn(reactiveValueOperations);
        Mockito.when(reactiveValueOperations.set(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Mono.just(Boolean.TRUE));
        Mockito.when(reactiveRedisTemplate.expire(Mockito.anyString(), Mockito.any()))
            .thenReturn(Mono.just(Boolean.TRUE));
        Mockito.when(reactiveValueOperations.get(Mockito.anyString()))
            .thenReturn(Mono.just("dummy-refresh-token"));

        // Mock UserServiceClient responses
        Mockito.when(userServiceClient.userAuthentication(Mockito.any(AuthenticationRequestDto.class)))
            .thenReturn(Mono.just(USER_INFO_RESPONSE_DTO));
        Mockito.when(userServiceClient.findByEmail(Mockito.any(UserEmailRequestDto.class)))
            .thenReturn(Mono.just(USER_INFO_RESPONSE_DTO));
    }

    @Test
    public void accessTokenExpiredTest() {
        TestUtils.mockUserService();
        JwtResponse jwtResponse = getToken();

        assertNotNull(jwtResponse);

        webTestClient.post()
            .uri(CLIENTS_URI)
            .header(AUTHORIZATION, jwtResponse.accessToken())
            .exchange()
            .expectStatus()
            .isUnauthorized();
    }

    @Test
    public void refreshTokenExpiredTest() {
        TestUtils.mockUserService();
        JwtResponse jwtResponse = getToken();

        assertNotNull(jwtResponse);

        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest(jwtResponse.refreshToken());

        webTestClient.post()
            .uri(REFRESH_URI)
            .bodyValue(refreshJwtRequest)
            .exchange()
            .expectStatus()
            .isUnauthorized();
    }

    private JwtResponse getToken() {
        return webTestClient.post()
            .uri(AUTHENTICATION_URI)
            .bodyValue(VALID_JWT_REQUEST)
            .exchange()
            .returnResult(JwtResponse.class)
            .getResponseBody()
            .blockFirst();
    }
}
