package com.innowise.swimdom.service;

import com.innowise.swimdom.dto.UserInfoResponseDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * A class for generating JWT tokens.
 */
@Slf4j
@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;

    private final SecretKey jwtRefreshSecret;

    private final int accessExpirationMinutes;

    private final int refreshExpirationDays;

    /**
     * The JwtProvider constructor.
     *
     * @param jwtAccessSecret secret key for the access token.
     * @param jwtRefreshSecret the secret key for the refresh token.
     * @param accessExpirationMinutes the validity period of the access token in minutes.
     * @param refreshExpirationDays the refresh token is valid in days.
     */
    public JwtProvider(
        @Value("${jwt.secret.access}") String jwtAccessSecret,
        @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
        @Value("${jwt.accessExpirationMinutes}") int accessExpirationMinutes,
        @Value("${jwt.refreshExpirationDays}") int refreshExpirationDays
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
    public String generateAccessToken(@NonNull UserInfoResponseDto userInfo) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(accessExpirationMinutes)
            .atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
            .subject(userInfo.email())
            .expiration(accessExpiration)
            .signWith(jwtAccessSecret)
            .claim("firstName", userInfo.firstname())
            .claim("lastName", userInfo.lastname())
            .claim("roles", userInfo.access())
            .compact();
    }

    /**
     * Generates a refresh token based on the transmitted user data.
     *
     * @param userInfo Map with user data.
     * @return the generated token.
     */
    public String generateRefreshToken(@NonNull UserInfoResponseDto userInfo) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(refreshExpirationDays)
            .atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
            .subject(userInfo.email())
            .expiration(refreshExpiration)
            .signWith(jwtRefreshSecret)
            .compact();
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
            log.error("invalid token", e);
        }
        return false;
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
