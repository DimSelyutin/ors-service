package com.innowise.swimdom.security;

import com.innowise.swimdom.service.impl.CustomUserDetailsService;
import com.innowise.swimdom.service.impl.JwtTokenProvider;
import com.innowise.swimdom.util.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new JWTAuthenticationFilter();

        setField(filter, "jwtTokenProvider", jwtTokenProvider);
        setField(filter, "customUserDetailsService", customUserDetailsService);

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
        when(request.getHeader(SecurityConstants.HEADER_STRING)).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_doesNotAuthenticate() throws ServletException, IOException {
        String token = "invalid.token";

        when(request.getHeader(SecurityConstants.HEADER_STRING)).thenReturn(
            SecurityConstants.TOKEN_PREFIX + " " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_exceptionInFilter_logsAndContinues() throws ServletException, IOException {
        String token = "some.token";

        when(request.getHeader(SecurityConstants.HEADER_STRING)).thenReturn(
            SecurityConstants.TOKEN_PREFIX + " " + token);
        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(token)).thenThrow(new RuntimeException("Test exception"));

        // Здесь проверим, что исключение не прерывает цепочку фильтров
        filter.doFilterInternal(request, response, filterChain);

        // Аутентификация не установлена
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);

        verify(filterChain).doFilter(request, response);
    }
}
