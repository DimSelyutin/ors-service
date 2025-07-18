package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.exception.UserTokenExpiredException;
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

    @Value("${constant.secret}")
    private String secret;

    @Value("${constant.expiration_time}")
    private Long expirationTime;

    public String generateToken(Authentication authentication) {
        log.debug("generateToken start - for {}", authentication.getName());
        User userDetails = (User) authentication.getPrincipal();
        String email = userDetails.getUsername();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(expirationTime);
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

    private Map<String, Object> buildClaims(User user) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", user.getId());
        claimsMap.put("email", user.getEmail());
        claimsMap.put("name", user.getName());
        claimsMap.put("lastname", user.getSurname());
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
