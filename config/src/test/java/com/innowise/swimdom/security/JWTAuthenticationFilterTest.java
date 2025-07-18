package com.innowise.swimdom.security;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.service.impl.CustomUserDetailsService;
import com.innowise.swimdom.service.impl.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JWTAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JWTAuthenticationFilter filter;

    @Value("${constant.header_string}")
    private String HEADER_STRING;

    @Value("${constant.header_string}")
    private String TOKEN_PREFIX;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new JWTAuthenticationFilter(jwtTokenProvider, customUserDetailsService);

        setField(filter, "headerString", "Authorization");
        setField(filter, "tokenPrefix", "Bearer ");

        SecurityContextHolder.clearContext();
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void doFilterInternal_noToken_doesNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader(HEADER_STRING)).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_doesNotAuthenticate() throws ServletException, IOException {
        String token = "invalid.token";

        when(request.getHeader(HEADER_STRING)).thenReturn(
            TOKEN_PREFIX + " " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_exceptionInFilter_logsAndContinues() throws ServletException, IOException {
        String token = "some.token";

        when(request.getHeader(HEADER_STRING)).thenReturn(
            TOKEN_PREFIX + " " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(token)).thenThrow(new RuntimeException("Test exception"));
        filter.doFilterInternal(request, response, filterChain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_validToken_authenticatesUser() throws ServletException, IOException {
        String jwt = "valid.token";
        UUID userId = UUID.randomUUID();
        User userDetails = mock(User.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        when(jwtTokenProvider.validateToken(jwt)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(jwt)).thenReturn(userId);
        when(customUserDetailsService.loadUserById(userId)).thenReturn((User) userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        when(customUserDetailsService.loadUserById(userId)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(auth);
        assertEquals(userDetails, auth.getPrincipal());

        verify(filterChain).doFilter(request, response);
    }
}