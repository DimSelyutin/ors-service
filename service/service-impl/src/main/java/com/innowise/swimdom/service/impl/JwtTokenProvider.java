package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.exception.UserTokenExpiredException;
import com.innowise.swimdom.util.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import static com.innowise.swimdom.util.Constants.TOKEN_INVALID;

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
        Map<String, Object> claimsMap = buildClaims(userDetails);
        log.debug("generateToken end - for {}", email);

        Instant now = Instant.now();
        Instant expiryInstant = now.plusSeconds(expirationTime);

        return Jwts.builder()
            .setSubject(email)
            .addClaims(claimsMap)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiryInstant))
            .signWith(getSigningKey())
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
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (IllegalArgumentException ex) {
            log.error("validateToken exception: {}", ex.getMessage());
            throw new UserTokenExpiredException(TOKEN_INVALID);
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

        String idStr = claims.get("id", String.class);
        return UUID.fromString(idStr);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
