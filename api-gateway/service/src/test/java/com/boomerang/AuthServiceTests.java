package com.innowise.swimdom;

import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.exception.FeignClientException;
import com.innowise.swimdom.exception.UnauthorizedException;
import com.innowise.swimdom.service.AuthService;
import com.innowise.swimdom.service.JwtProvider;
import com.innowise.swimdom.service.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

import static com.innowise.swimdom.util.TestConstants.ACCESS_TOKEN;
import static com.innowise.swimdom.util.TestConstants.INVALID_EMAIL;
import static com.innowise.swimdom.util.TestConstants.INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE;
import static com.innowise.swimdom.util.TestConstants.INVALID_PASSWORD;

import static com.innowise.swimdom.util.TestConstants.REFRESH_TOKEN;
import static com.innowise.swimdom.util.TestConstants.RESOURCE_NOT_AVAILABLE_ERROR_MESSAGE;
import static com.innowise.swimdom.util.TestConstants.TOKEN_MAP;
import static com.innowise.swimdom.util.TestConstants.UNAUTHORIZED_EXCEPTION;
import static com.innowise.swimdom.util.TestConstants.USER_INFO_MONO;


import static com.innowise.swimdom.util.TestConstants.VALID_EMAIL;
import static com.innowise.swimdom.util.TestConstants.VALID_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Тест проверяет корректность методов класса AuthService.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {
    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Mock
    private ReactiveValueOperations<String, String> reactiveValueOperations;

    @InjectMocks
    private AuthService authService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    public void beforeEach() {
        ReflectionTestUtils.setField(authService, "refreshExpirationDays", 30);
    }

    @Test
    public void authorizationTest() {
        when(jwtProvider.generateAccessToken(Mockito.any())).thenReturn(ACCESS_TOKEN);
        when(jwtProvider.generateRefreshToken(Mockito.any())).thenReturn(REFRESH_TOKEN);
        when(userServiceClient.userAuthentication(new AuthenticationRequestDto(VALID_EMAIL,VALID_PASSWORD)))
            .thenReturn(USER_INFO_MONO);
        when(reactiveRedisTemplate.opsForValue()).thenReturn(reactiveValueOperations);
        when(reactiveValueOperations.set(Mockito.anyString(), Mockito.anyString())).thenReturn(Mono.just(true));
        when(reactiveRedisTemplate.expire(Mockito.anyString(), Mockito.any(Duration.class)))
            .thenReturn(Mono.just(true));

        Mono<Map<String, Object>> actual = authService.login(new AuthenticationRequestDto(VALID_EMAIL, VALID_PASSWORD));
        assertEquals(TOKEN_MAP, actual.block());
    }

    @Test
    public void invalidEmailAuthorizationTest() {
        when(userServiceClient.userAuthentication(not(eq(new AuthenticationRequestDto(VALID_EMAIL,VALID_PASSWORD)))))
            .thenReturn(Mono.error(UNAUTHORIZED_EXCEPTION));

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
                authService.login(new AuthenticationRequestDto(INVALID_EMAIL, VALID_PASSWORD)).block());
        assertEquals(INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    public void invalidPasswordAuthorizationTest() {
        when(userServiceClient.userAuthentication(not(eq(new AuthenticationRequestDto(VALID_EMAIL,VALID_PASSWORD)))))
            .thenReturn(Mono.error(UNAUTHORIZED_EXCEPTION));

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
            authService.login(new AuthenticationRequestDto(VALID_EMAIL, INVALID_PASSWORD)).block());
        assertEquals(INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    public void resourceNotAvailableTest() {
        when(userServiceClient.userAuthentication(new AuthenticationRequestDto(VALID_EMAIL,VALID_PASSWORD)))
            .thenReturn(Mono.error(new FeignClientException(RESOURCE_NOT_AVAILABLE_ERROR_MESSAGE)));


        FeignClientException exception = assertThrows(FeignClientException.class, () ->
            authService.login(new AuthenticationRequestDto(VALID_EMAIL, VALID_PASSWORD)).block());
        assertEquals(RESOURCE_NOT_AVAILABLE_ERROR_MESSAGE, exception.getMessage());
    }
}
