package com.innowise.swimdom.service;

import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.UserEmailRequestDto;
import com.innowise.swimdom.dto.UserInfoResponseDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * Interface for accessing the userservice.
 */
@ReactiveFeignClient(name = "userservice", url = "${services.userservice.url}", path = "${services.userservice.path}",
                     fallbackFactory = UserServiceFallbackFactory.class)
public interface UserServiceClient {

    /**
     * Authorizes the user by email and password.
     *
     * @param authenticationRequestDto user's email and password.
     * @return Mono{@literal <}Map{@literal >} with user data.
     */
    @PostMapping(
        value = "/user/userAuthentication",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<UserInfoResponseDto> userAuthentication(AuthenticationRequestDto authenticationRequestDto);


    /**
     * Requests an authorized user by email.
     *
     * @param userEmailRequestDto the user's email.
     * @return Mono{@literal <}Map{@literal >} with user data.
     */
    @PostMapping(
        value = "/user/findByEmail",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<UserInfoResponseDto> findByEmail(UserEmailRequestDto userEmailRequestDto);
}
