package com.innowise.swimdom.util;

import com.innowise.swimdom.dto.JwtResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class with constants for testing.
 */
public class TestConstants {
    /** JWT access token. */
    public static final String ACCESS_TOKEN = "access_token";

    /** JWT refresh token. */
    public static final String REFRESH_TOKEN = "refresh_token";

    /** HashMap with access and refresh tokens. */
    public static final Map<String, Object> ACCESS_REFRESH_TOKEN_MAP = new HashMap<>() {{
            put("accessToken", ACCESS_TOKEN);
            put("refreshToken", REFRESH_TOKEN);
        }};

    /** JwtResponse with access and refresh tokens. */
    public static final JwtResponse RESPONSE_DTO = new JwtResponse(ACCESS_TOKEN, REFRESH_TOKEN);
}
