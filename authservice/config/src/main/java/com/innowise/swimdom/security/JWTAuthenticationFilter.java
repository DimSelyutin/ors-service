package com.innowise.swimdom.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.innowise.swimdom.service.impl.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * JWTAuthenticationFilter.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${token.prefix}")
    private String tokenPrefix;

    @Value("${token.header_string}")
    private String headerString;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String accessToken = getJWTFromRequest(request);

        if (accessToken != null && jwtTokenProvider.isAccessTokenValid(accessToken)) {
            try {

                Claims claims = jwtTokenProvider.getAccessClaims(accessToken);
                String email = claims.getSubject();
                List<String> roles = (List<String>) claims.get("roles");
                List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ex) {
                log.error("Authentication failed: {}", ex.getLocalizedMessage());
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
