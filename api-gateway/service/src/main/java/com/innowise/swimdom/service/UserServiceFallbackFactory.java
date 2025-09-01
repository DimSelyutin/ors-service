package com.innowise.swimdom.service;

import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.UserEmailRequestDto;
import com.innowise.swimdom.dto.UserInfoResponseDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactivefeign.FallbackFactory;
import reactor.core.publisher.Mono;

/**
 * Backup implementation of UserServiceClient. Circuit Breaker.
 */
@Slf4j
@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserServiceClient> {

    @Value("${api-gateway.isStubEnabled}")
    private boolean isEnable;

    @Override
    public UserServiceClient apply(Throwable throwable) {
        return new UserServiceClient() {
            @Override
            public Mono<UserInfoResponseDto> userAuthentication(
                AuthenticationRequestDto authenticationRequestDto) {
                if (Boolean.TRUE.equals(isThrowError())) {
                    return Mono.error(throwable);
                }
                log.info(
                    "UserServiceClientFallback: userAuthorization fallback session with email: "
                        + authenticationRequestDto.email());
                return createTestMap();
            }

            @Override
            public Mono<UserInfoResponseDto> findByEmail(UserEmailRequestDto userEmailRequestDto) {
                if (Boolean.TRUE.equals(isThrowError())) {
                    return Mono.error(throwable);
                }
                log.info("UserServiceClientFallback: findByEmail fallback session with email: "
                    + userEmailRequestDto.email());
                return createTestMap();
            }

            private Boolean isThrowError() {
                return !isEnable || throwable instanceof FeignException.Unauthorized;
            }
        };
    }

    private Mono<UserInfoResponseDto> createTestMap() {
        return Mono.just(new UserInfoResponseDto(
            "stepanov@mail.ru ",
            "Stepan",
            "Stepanov",
            "ADMIN"));
    }
}
