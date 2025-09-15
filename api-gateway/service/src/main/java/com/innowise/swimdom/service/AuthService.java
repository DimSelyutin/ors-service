package com.innowise.swimdom.service;

import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.JwtResponse;
import com.innowise.swimdom.dto.UserEmailRequestDto;
import com.innowise.swimdom.dto.UserInfoResponseDto;
import com.innowise.swimdom.exception.IncorrectRefreshTokenException;
import com.innowise.swimdom.exception.NoContentException;
import com.innowise.swimdom.exception.RedisSaveException;
import com.innowise.swimdom.mapper.JwtResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
import static java.lang.Boolean.TRUE;

/**
 * Authentication service.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String REFRESH_TOKEN = "refreshToken";

    private static final String ACCESS_TOKEN = "accessToken";

    @Value(value = "${jwt.refreshExpirationDays}")
    private Integer refreshExpirationDays;

    private final JwtProvider jwtProvider;

    private final UserServiceClient userServiceClient;

    private final JwtResponseMapper jwtMapper;

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    /**
     * Returns JWT tokens based on the transmitted username and password.
     *
     * @param authRequestDto the user's email.
     * @return Mono{@literal <}Map{@literal >} with access and refresh tokens.
     */
    public JwtResponse login(AuthenticationRequestDto authRequestDto) {
        UserInfoResponseDto user = userServiceClient.userAuthentication(
            new AuthenticationRequestDto(authRequestDto.email(), authRequestDto.password()))
            ;
        Map<String, Object> tokens = generateTokens(user);
        Map<String, Object> saved = saveRefreshTokenInRedis(tokens, authRequestDto.email());
        return jwtMapper.mapToDto(saved);
    }

    /**
     * The JWT token is returned with new access and refresh tokens.
     *
     * @param refreshToken refresh token.
     * @return Mono {@literal <}Map{@literal >} with access and refresh tokens.
     */
    public JwtResponse getNewAccessToken(String refreshToken) {
        if (jwtProvider.isRefreshTokenValid(refreshToken)) {
            String email = jwtProvider.getRefreshClaims(refreshToken).getSubject();
            String tokenFromRedis = getRefreshTokenFromRedis(email);
            UserInfoResponseDto userData = tokenEquivalenceCheck(email, tokenFromRedis, refreshToken);
            Map<String, Object> tokens = generateTokens(userData);
            Map<String, Object> savedTokens = saveRefreshTokenInRedis(tokens, email);
            return jwtMapper.mapToDto(savedTokens);
        }
        throw new IncorrectRefreshTokenException("Invalid refresh token");
    }

    private Map<String, Object> saveRefreshTokenInRedis(Map<String, Object> map, String key) {
        Boolean success = reactiveRedisTemplate.opsForValue()
            .set(key, (String) map.get(REFRESH_TOKEN))
            .block();
        if (TRUE.equals(success)) {
            reactiveRedisTemplate.expire(key, Duration.ofDays(refreshExpirationDays)).block();
            return map;
        } else {
            throw new RedisSaveException("Failed to save refresh token to Redis");
        }
    }

    private String getRefreshTokenFromRedis(String key) {
        String token = reactiveRedisTemplate.opsForValue().get(key).block();
        if (token == null) {
            throw new IncorrectRefreshTokenException("Invalid refresh token");
        }
        return token;
    }

    private UserInfoResponseDto tokenEquivalenceCheck(String email, String expectedToken, String actualToken) {
        if (!actualToken.equals(expectedToken)) {
            throw new IncorrectRefreshTokenException("Invalid refresh token");
        }
        UserInfoResponseDto user = userServiceClient.findByEmail(new UserEmailRequestDto(email));
        if (user == null) {
            throw new NoContentException("User not found");
        }
        return user;
    }

    private Map<String, Object> generateTokens(UserInfoResponseDto userData) {
        return Map.of(ACCESS_TOKEN, jwtProvider.generateAccessToken(userData),
            REFRESH_TOKEN, jwtProvider.generateRefreshToken(userData));
    }
}
