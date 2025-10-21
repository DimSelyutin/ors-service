package com.innowise.swimdom.service;

import com.innowise.swimdom.exception.BadRequestException;
import com.innowise.swimdom.exception.FeignClientException;
import com.innowise.swimdom.exception.ForbiddenException;
import com.innowise.swimdom.exception.NotFoundException;
import com.innowise.swimdom.exception.ServiceUnavailableException;
import com.innowise.swimdom.exception.UnauthorizedException;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Error decoder for feign.
 */
@Configuration
public class FeignClientErrorDecoder implements ErrorDecoder {

    @Bean
    public UserServiceFallbackFactory userServiceFallbackFactory() {
        return new UserServiceFallbackFactory();
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new BadRequestException("Syntax error in the request");
            case 404 -> new NotFoundException("User not found");
            case 401 -> new UnauthorizedException("Invalid email or password");
            case 403 -> new ForbiddenException("The server cannot execute the request");
            case 503 -> new ServiceUnavailableException("Service unavailable");
            default -> new FeignClientException("Internal server error");
        };
    }
}
