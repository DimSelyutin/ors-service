package com.innowise.swimdom;

import com.innowise.swimdom.dto.ErrorDto;
import com.innowise.swimdom.dto.JwtResponse;
import com.innowise.swimdom.dto.RefreshJwtRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import static com.innowise.swimdom.util.TestConstants.AUTHENTICATION_URI;
import static com.innowise.swimdom.util.TestConstants.INVALID_EMAIL_JWT_REQUEST;
import static com.innowise.swimdom.util.TestConstants.INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE;
import static com.innowise.swimdom.util.TestConstants.INVALID_PASSWORD_JWT_REQUEST;
import static com.innowise.swimdom.util.TestConstants.INVALID_REFRESH_TOKEN;
import static com.innowise.swimdom.util.TestConstants.REFRESH_URI;
import static com.innowise.swimdom.util.TestConstants.USER_AUTH_URI;
import static com.innowise.swimdom.util.TestConstants.USER_EMAIL_REQUEST_DTO;
import static com.innowise.swimdom.util.TestConstants.USER_FIND_URI;
import static com.innowise.swimdom.util.TestConstants.USER_INFO_RESPONSE_DTO;
import static com.innowise.swimdom.util.TestConstants.VALID_AUTHENTICATION_REQUEST_DTO;
import static com.innowise.swimdom.util.TestConstants.VALID_JWT_REQUEST;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.unauthorized;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import com.innowise.swimdom.service.UserServiceClient;
import com.innowise.swimdom.service.UserServiceFallbackFactory;
import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.UserEmailRequestDto;
import reactor.core.publisher.Mono;

/**
 * API Gateway integration tests.
 */
@SpringBootTest(properties = {
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false",
    "spring.main.allow-bean-definition-overriding=true"
})
@AutoConfigureWebTestClient(timeout = "36000")
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
@EnableReactiveFeignClients
@Import({})
class ApiGatewayApplicationTests {

    private final ObjectMapper mapper = new ObjectMapper();

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
    public void loginTest() {
        mockUserServiceAuth();

        JwtResponse jwtResponse = webTestClient.post()
                .uri(AUTHENTICATION_URI)
                .bodyValue(VALID_JWT_REQUEST)
                .exchange()
                .returnResult(JwtResponse.class)
                .getResponseBody()
                .blockFirst();

        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.accessToken());
        assertNotNull(jwtResponse.refreshToken());
    }

    @Test
    public void loginTestWithInvalidEmail() {
        mockUserServiceInvalidAuth();

        ErrorDto errorDto = webTestClient.post()
                .uri(AUTHENTICATION_URI)
                .bodyValue(INVALID_EMAIL_JWT_REQUEST)
                .exchange()
                .returnResult(ErrorDto.class)
                .getResponseBody()
                .blockFirst();

        assertNotNull(errorDto);
        assertNotNull(errorDto.timestamp());
        assertEquals(401, errorDto.errorCode());
        assertEquals(INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE, errorDto.errorMessage());
    }

    @Test
    public void loginTestWithInvalidPassword() {
        mockUserServiceInvalidAuth();

        ErrorDto errorDto = webTestClient.post()
                .uri(AUTHENTICATION_URI)
                .bodyValue(INVALID_PASSWORD_JWT_REQUEST)
                .exchange()
                .returnResult(ErrorDto.class)
                .getResponseBody()
                .blockFirst();

        assertNotNull(errorDto);
        assertNotNull(errorDto.timestamp());
        assertEquals(401, errorDto.errorCode());
        assertEquals(INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE, errorDto.errorMessage());
    }

    @Test
    void serviceUnavailableTest() {
        mockUserServiceUnavailable();

        JwtResponse jwtResponse = webTestClient.post()
                .uri(AUTHENTICATION_URI)
                .bodyValue(VALID_JWT_REQUEST)
                .exchange()
                .returnResult(JwtResponse.class)
                .getResponseBody()
                .blockFirst();

        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.accessToken());
        assertNotNull(jwtResponse.refreshToken());
    }

    @Test
    void testShouldReturnUpdatedRefreshAndAccessTokens() {
        mockUserServiceAuth();
        mockUserServiceFindByEmail();

        JwtResponse jwtResponse = webTestClient.post()
                .uri(AUTHENTICATION_URI)
                .bodyValue(VALID_JWT_REQUEST)
                .exchange()
                .returnResult(JwtResponse.class)
                .getResponseBody()
                .blockFirst();

        assert jwtResponse != null;
        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest(jwtResponse.refreshToken());

        JwtResponse updatedJwtResponse = webTestClient.post()
                .uri(REFRESH_URI)
                .bodyValue(refreshJwtRequest)
                .exchange()
                .returnResult(JwtResponse.class)
                .getResponseBody()
                .blockFirst();

        assertNotNull(updatedJwtResponse);
        assertNotNull(updatedJwtResponse.accessToken());
        assertNotNull(updatedJwtResponse.refreshToken());
    }

    @Test
    void testShouldReturnUnauthorizedExceptionIfJwtRefreshTokenInvalid() {
        mockUserServiceFindByEmail();

        RefreshJwtRequest refreshJwtRequest = new RefreshJwtRequest(INVALID_REFRESH_TOKEN);

        ErrorDto errorDto = webTestClient.post()
                .uri(REFRESH_URI)
                .bodyValue(refreshJwtRequest)
                .exchange()
                .returnResult(ErrorDto.class)
                .getResponseBody()
                .blockFirst();

        assertNotNull(errorDto);
        assertNotNull(errorDto.timestamp());
        assertEquals(401, errorDto.errorCode());
    }

    private void mockUserServiceAuth() {
        String jsonUser;
        String jsonAuthorizationRequestDto;
        try {
            jsonUser = mapper.writeValueAsString(USER_INFO_RESPONSE_DTO);
            jsonAuthorizationRequestDto = mapper.writeValueAsString(VALID_AUTHENTICATION_REQUEST_DTO);
        } catch (JsonProcessingException e) {
            jsonUser = null;
            jsonAuthorizationRequestDto = null;
        }

        stubFor(post(USER_AUTH_URI)
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.ACCEPT, containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(jsonAuthorizationRequestDto))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonUser)));
    }

    private void mockUserServiceInvalidAuth() {
        stubFor(post(USER_AUTH_URI).willReturn(unauthorized()));
    }

    private void mockUserServiceFindByEmail() {
        String jsonUser;
        String jsonUserEmailRequestDto;
        try {
            jsonUser = mapper.writeValueAsString(USER_INFO_RESPONSE_DTO);
            jsonUserEmailRequestDto = mapper.writeValueAsString(USER_EMAIL_REQUEST_DTO);
        } catch (JsonProcessingException e) {
            jsonUser = null;
            jsonUserEmailRequestDto = null;
        }

        stubFor(post(USER_FIND_URI)
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.ACCEPT, containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(jsonUserEmailRequestDto))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonUser)));
    }

    private void mockUserServiceUnavailable() {
        stubFor(post(urlPathMatching(USER_AUTH_URI))
                .willReturn(serviceUnavailable()));
    }
}
