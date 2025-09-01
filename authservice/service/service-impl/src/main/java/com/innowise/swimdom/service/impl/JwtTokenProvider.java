package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.openapi.model.UserResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.innowise.swimdom.util.Constants.TOKEN_INVALID;

/**
 * JwtTokenProvider.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey jwtAccessSecret;

    private final SecretKey jwtRefreshSecret;

    private final int accessExpirationMinutes;

    private final int refreshExpirationDays;

    /**
     * The JwtProvider constructor.
     *
     * @param jwtAccessSecret         secret key for the access token.
     * @param jwtRefreshSecret        the secret key for the refresh token.
     * @param accessExpirationMinutes the validity period of the access token in minutes.
     * @param refreshExpirationDays   the refresh token is valid in days.
     */
    public JwtTokenProvider(
        @Value("${token.secret.access}") String jwtAccessSecret,
        @Value("${token.secret.refresh}") String jwtRefreshSecret,
        @Value("${token.accessExpirationMinutes}") int accessExpirationMinutes,
        @Value("${token.refreshExpirationDays}") int refreshExpirationDays
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.accessExpirationMinutes = accessExpirationMinutes;
        this.refreshExpirationDays = refreshExpirationDays;
    }

    /**
     * Generates an access token based on the transmitted user data.
     *
     * @param userInfo Map with user data.
     * @return the generated token.
     */
    public String generateAccessToken(@NonNull UserResponseDTO userInfo) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(accessExpirationMinutes)
            .atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
            .subject(userInfo.getEmail())
            .expiration(accessExpiration)
            .signWith(jwtAccessSecret)
            .claim("firstName", userInfo.getName())
            .claim("lastName", userInfo.getPhone())
            .claim("roles", userInfo.getRole().getValue())
            .compact();
    }

    /**
     * Generates a refresh token based on the transmitted user data.
     *
     * @param userInfo Map with user data.
     * @return the generated token.
     */
    public String generateRefreshToken(@NonNull UserResponseDTO userInfo) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(refreshExpirationDays)
            .atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
            .subject(userInfo.getEmail())
            .expiration(refreshExpiration)
            .signWith(jwtRefreshSecret)
            .compact();
    }

    public String getEmailFromToken(String jwt) {
        return (String) getClaims(jwt, jwtAccessSecret).get("email");
    }

    /**
     * Returns fields encrypted in the access token.
     *
     * @param token token.
     * @return field values.
     */
    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    /**
     * Returns fields encrypted in the refresh token.
     *
     * @param token token.
     * @return field values.
     */
    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private boolean isTokenValid(@NotNull String token, @NonNull SecretKey secret) {
        try {
            Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException signatureException) {
            log.error("Invalid signature", signatureException);
        } catch (Exception e) {
            log.error(TOKEN_INVALID, e);
        }
        return false;
    }

    public Duration getRefreshTokenExpirationTime() {
        return Duration.ofDays(refreshExpirationDays);
    }

    /**
     * A method for validating the refresh token.
     *
     * @param refreshToken token
     * @return boolean
     */
    public boolean isRefreshTokenValid(String refreshToken) {
        return isTokenValid(refreshToken, jwtRefreshSecret);
    }

    /**
     * A method for validating an access token.
     *
     * @param accessToken token
     * @return boolean
     */
    public boolean isAccessTokenValid(String accessToken) {
        return isTokenValid(accessToken, jwtAccessSecret);
    }
}
