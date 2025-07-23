package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.exception.UserTokenExpiredException;
import com.innowise.swimdom.util.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

/**
 * JwtTokenProvider.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration_time}")
    private Long expirationTime;

    public String generateToken(Authentication authentication) {
        log.debug("generateToken start - for {}", authentication.getName());
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusSeconds(expirationTime);
        Map<String, Object> claimsMap = buildClaims(userDetails);
        log.debug("generateToken end - for {}", email);

        return Jwts.builder()
            .setSubject(email)
            .addClaims(claimsMap)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.from(expiryDate)))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    private Map<String, Object> buildClaims(CustomUserDetails user) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", user.user().getId());
        claimsMap.put("email", user.user().getEmail());
        claimsMap.put("name", user.user().getName());
        claimsMap.put("lastname", user.user().getSurname());
        return claimsMap;
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                .setSigningKey(secret)
                .build().parseClaimsJws(token);
            return true;
        } catch (IllegalArgumentException ex) {
            log.error("validateToken exception: {}", ex.getMessage());
            throw new UserTokenExpiredException("Token expired!");
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .build().parseClaimsJws(token)
            .getBody();
        UUID id = (UUID) claims.get("id");
        return id;
    }
}
