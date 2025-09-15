package com.innowise.swimdom.service;

import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.UserEmailRequestDto;
import com.innowise.swimdom.dto.UserInfoResponseDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * Backup implementation of UserServiceClient. Circuit Breaker.
 */
@Slf4j
@Service
public class UserServiceFallbackFactory implements FallbackFactory<UserServiceClient> {

    @Value("${api-gateway.isStubEnabled}")
    private boolean isEnable;

    @Override
    public UserServiceClient create(Throwable throwable) {
        return new UserServiceClient() {
            @Override
            public UserInfoResponseDto userAuthentication(
                AuthenticationRequestDto authenticationRequestDto) {
                if (Boolean.TRUE.equals(isThrowError())) {
                    throwAsRuntime(throwable);
                }
                log.info(
                    "UserServiceClientFallback: userAuthorization fallback session with email: "
                        + authenticationRequestDto.email());
                return createTestUser();
            }

            @Override
            public UserInfoResponseDto findByEmail(UserEmailRequestDto userEmailRequestDto) {
                if (Boolean.TRUE.equals(isThrowError())) {
                    throwAsRuntime(throwable);
                }
                log.info("UserServiceClientFallback: findByEmail fallback session with email: "
                    + userEmailRequestDto.email());
                return createTestUser();
            }

            private Boolean isThrowError() {
                return !isEnable || throwable instanceof FeignException.Unauthorized;
            }
        };
    }

    private UserInfoResponseDto createTestUser() {
        return new UserInfoResponseDto(
            "stepanov@mail.ru ",
            "Stepan",
            "Stepanov",
            "ADMIN");
    }

    private void throwAsRuntime(Throwable throwable) {
        if (throwable instanceof RuntimeException runtime) {
            throw runtime;
        }
        throw new RuntimeException(throwable);
    }
}
