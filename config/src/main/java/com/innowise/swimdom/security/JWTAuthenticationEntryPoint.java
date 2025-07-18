package com.innowise.swimdom.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWTAuthenticationEntryPoint.
 */
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${constant.content_type}")
    private String contentType;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(contentType);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(authException.getLocalizedMessage());
    }

}
