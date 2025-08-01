package com.innowise.swimdom.util;

import java.util.List;

/**
 * Class with constants.
 */
public final class Constants {

    private Constants() {
    }

    public static final List<String> PUBLIC_URLS = List.of(
        "/api/v1/auth/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/actuator"
    );

    public static final List<String> ADMIN_URLS = List.of(
        "api/v1/admin/**"
    );
    public static final List<String> ALLOWED_ORIGIN_PATTERNS = List.of("*");

    public static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");

    public static final List<String> ALLOWED_HEADERS = List.of("*");

    public static final String INVALID_CREDENTIAL = "Wrong login or password!";
    public static final String TOKEN_INVALID = "Token expired or invalid!";
    public static final String ROLE_PREFIX = "ROLE_";

    public static final String USER_NOT_FOUND = "User not found with ID: ";
    public static final String BOOKING_NOT_FOUND = "Booking not found with ID: ";
    public static final String POOL_NOT_FOUND = "Pool not found with ID: ";
}
