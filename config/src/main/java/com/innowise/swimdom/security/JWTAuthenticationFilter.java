package com.innowise.swimdom.security;

import java.io.IOException;
import java.util.UUID;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.service.impl.CustomUserDetailsService;
import com.innowise.swimdom.service.impl.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWTAuthenticationFilter.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${constant.token_prefix}")
    private String tokenPrefix;

    @Value("${constant.header_string}")
    private String headerString;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String jwt = getJWTFromRequest(request);
        UUID userId = null;
        User userDetails = null;

        if (jwt != null) {
            try {
                if (jwtTokenProvider.validateToken(jwt)) {
                    userId = jwtTokenProvider.getUserIdFromToken(jwt);
                    userDetails = (User) customUserDetailsService.loadUserById(userId);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException ex) {
                log.error("JWT token is expired: {}", ex.getMessage());
            } catch (UnsupportedJwtException ex) {
                log.error("JWT token is unsupported: {}", ex.getMessage());
            } catch (MalformedJwtException ex) {
                log.error("Invalid JWT token: {}", ex.getMessage());
            } catch (SignatureException ex) {
                log.error("Invalid JWT signature: {}", ex.getMessage());
            } catch (IllegalArgumentException ex) {
                log.error("JWT claims string is empty: {}", ex.getMessage());
            } catch (UsernameNotFoundException ex) {
                log.error("User ID from JWT not found: {}", ex.getMessage());
            } catch (Exception ex) {
                log.error("Could not set user authentication: {}", ex.getLocalizedMessage(),
                    ex);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(headerString);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
            String[] parts = bearerToken.split(" ");
            if (parts.length == 2) {
                return parts[1];
            }
        }
        return null;
    }
}
