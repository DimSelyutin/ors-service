package com.innowise.swimdom.service;

import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.UserEmailRequestDto;
import com.innowise.swimdom.dto.UserInfoResponseDto;
import com.innowise.swimdom.exception.BadRequestException;
import com.innowise.swimdom.exception.FeignClientException;
import com.innowise.swimdom.exception.ForbiddenException;
import com.innowise.swimdom.exception.IncorrectRefreshTokenException;
import com.innowise.swimdom.exception.NoContentException;
import com.innowise.swimdom.exception.NotFoundException;
import com.innowise.swimdom.exception.RedisSaveException;
import com.innowise.swimdom.exception.ServiceUnavailableException;
import com.innowise.swimdom.exception.UnauthorizedException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    /**
     * Returns JWT tokens based on the transmitted username and password.
     *
     * @param authRequestDto the user's email.
     * @return Mono{@literal <}Map{@literal >} with access and refresh tokens.
     */
    public Mono<Map<String, Object>> login(AuthenticationRequestDto authRequestDto) {
        return userServiceClient.userAuthentication(
                new AuthenticationRequestDto(authRequestDto.email(), authRequestDto.password()))
            .map(this::generateTokens)
            .flatMap(map -> saveRefreshTokenInRedis(map, authRequestDto.email()))
            .doOnError(this::handleException);
    }

    /**
     * The JWT token is returned with new access and refresh tokens.
     *
     * @param refreshToken refresh token.
     * @return Mono {@literal <}Map{@literal >} with access and refresh tokens.
     */
    public Mono<Map<String, Object>> getNewAccessToken(String refreshToken) {
        if (jwtProvider.isRefreshTokenValid(refreshToken)) {
            String email = jwtProvider.getRefreshClaims(refreshToken).getSubject();
            return getRefreshTokenFromRedis(email)
                .flatMap(token -> tokenEquivalenceCheck(email, token, refreshToken))
                .map(this::generateTokens)
                .flatMap(map -> saveRefreshTokenInRedis(map, email))
                .doOnError(this::handleException);
        }
        return Mono.error(new IncorrectRefreshTokenException());
    }

    private Mono<Map<String, Object>> saveRefreshTokenInRedis(Map<String, Object> map, String key) {
        return reactiveRedisTemplate.opsForValue().set(key, (String) map.get(REFRESH_TOKEN))
            .flatMap(success -> {
                if (TRUE.equals(success)) {
                    return reactiveRedisTemplate.expire(key, Duration.ofDays(refreshExpirationDays))
                        .flatMap(x -> Mono.just(map));
                } else {
                    return Mono.error(new RedisSaveException());
                }
            });
    }

    private Mono<String> getRefreshTokenFromRedis(String key) {
        return reactiveRedisTemplate.opsForValue().get(key)
            .switchIfEmpty(Mono.error(new IncorrectRefreshTokenException()));
    }

    private Mono<UserInfoResponseDto> tokenEquivalenceCheck(String email, String expectedToken, String actualToken) {
        if (actualToken.equals(expectedToken)) {
            return userServiceClient.findByEmail(new UserEmailRequestDto(email))
                .switchIfEmpty(Mono.error(new NoContentException()));
        } else {
            return Mono.error(new IncorrectRefreshTokenException());
        }
    }

    private Map<String, Object> generateTokens(UserInfoResponseDto userData) {
        return Map.of(ACCESS_TOKEN, jwtProvider.generateAccessToken(userData),
            REFRESH_TOKEN, jwtProvider.generateRefreshToken(userData));
    }

    private void handleException(Throwable e) {
        if (e instanceof FeignException feignException && feignException.status() == 404) {
            throw new NotFoundException("User not found", e);
        }
        if (e instanceof FeignException.ServiceUnavailable) {
            throw new ServiceUnavailableException("Service unavailable", e);
        }
        if (e instanceof FeignException.BadRequest) {
            throw new BadRequestException("Syntax error in the request", e);
        }
        if (e instanceof FeignException.Unauthorized) {
            throw new UnauthorizedException("Invalid email or password", e);
        }
        if (e instanceof FeignException.Forbidden) {
            throw new ForbiddenException("The server cannot execute the request, rights are insufficient", e);
        }
        if (e instanceof FeignException) {
            throw new FeignClientException("Internal server error", e);
        }
    }
}
