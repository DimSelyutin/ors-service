package com.innowise.swimdom.security;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.innowise.swimdom.util.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.io.PrintWriter;
import java.io.StringWriter;

class JWTAuthenticationEntryPointTest {

    private JWTAuthenticationEntryPoint entryPoint;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationException authException;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        entryPoint = new JWTAuthenticationEntryPoint();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authException = mock(AuthenticationException.class);

        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);

        when(response.getWriter()).thenReturn(printWriter);
        when(authException.getLocalizedMessage()).thenReturn("Unauthorized access");
    }

    @Test
    void commence_shouldSetUnauthorizedStatusAndWriteMessage() throws Exception {
        entryPoint.commence(request, response, authException);

        verify(response).setContentType(SecurityConstants.CONTENT_TYPE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Flush writer to make sure content is written
        response.getWriter().flush();

        String responseContent = responseWriter.toString();
        assertTrue(responseContent.contains("Unauthorized access"));
    }
}
