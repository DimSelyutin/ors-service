package com.innowise.swimdom.util;

/**
 * Class with constants.
 */
public class SecurityConstants {
    public static final String SIGN_UP_URL = "api/auth/*";
    public static final String SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final int EXPIRATION_TIME = 600_000;

}
