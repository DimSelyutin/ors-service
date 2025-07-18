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

}
