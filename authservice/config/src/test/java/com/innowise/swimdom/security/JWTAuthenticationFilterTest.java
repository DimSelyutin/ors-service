package com.innowise.swimdom.security;

import com.innowise.swimdom.service.impl.CustomUserDetailsService;
import com.innowise.swimdom.service.impl.JwtTokenProvider;
import io.jsonwebtoken.Claims;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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

    @Value("${token.header_string}")
    private String HEADER_STRING;

    @Value("${token.header_string}")
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
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_doesNotAuthenticate() throws ServletException, IOException {
        String token = "invalid.token";

        when(request.getHeader(HEADER_STRING)).thenReturn(
            TOKEN_PREFIX + " " + token);
        when(jwtTokenProvider.isAccessTokenValid(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_exceptionInFilter_logsAndContinues() throws ServletException, IOException {
        String token = "some.token";

        when(request.getHeader(HEADER_STRING)).thenReturn(
            TOKEN_PREFIX + " " + token);
        when(jwtTokenProvider.isAccessTokenValid(token)).thenReturn(true);
        when(jwtTokenProvider.getEmailFromToken(token)).thenThrow(new RuntimeException("Test exception"));
        filter.doFilterInternal(request, response, filterChain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_validToken_authenticatesUser() throws ServletException, IOException {
        String jwt = "valid.token";
        String userEmail = "user@example.com";
        List<String> userRoles = List.of("ROLE_USER");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtTokenProvider.isAccessTokenValid(jwt)).thenReturn(true);
        Claims claims = mock(Claims.class);
        when(jwtTokenProvider.getAccessClaims(jwt)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(userEmail);
        when(claims.get("roles")).thenReturn(userRoles);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(auth, "Authentication context should not be null");

        assertEquals(userEmail, auth.getPrincipal(), "Principal should be the user's email");

        List<String> expectedAuthorities = new ArrayList<>(userRoles);
        List<String> actualAuthorities = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        assertEquals(expectedAuthorities, actualAuthorities, "Authorities should match mocked roles");

        verify(filterChain, times(1)).doFilter(request, response);
    }
}