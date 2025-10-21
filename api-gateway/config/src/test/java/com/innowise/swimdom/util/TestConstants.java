package com.innowise.swimdom.util;

import com.innowise.swimdom.dto.AuthenticationRequestDto;
import com.innowise.swimdom.dto.JwtRequest;
import com.innowise.swimdom.dto.UserEmailRequestDto;

import com.innowise.swimdom.dto.UserInfoResponseDto;

/**
 * Utility class with constants for testing.
 */
public class TestConstants {
    /** Valid email. */
    public static final String VALID_EMAIL = "ivanov@mail.ru";

    /** Valid password. */
    public static final String VALID_PASSWORD = "qwerty";

    /** Invalid email. */
    public static final String INVALID_EMAIL = "notivanov123@mail.ru";

    /** Invalid password. */
    public static final String INVALID_PASSWORD = "12345";

    /** JwtRequest with valid credentials. */
    public static final JwtRequest VALID_JWT_REQUEST = new JwtRequest(VALID_EMAIL, VALID_PASSWORD);

    /** JwtRequest with invalid email. */
    public static final JwtRequest INVALID_EMAIL_JWT_REQUEST = new JwtRequest(INVALID_EMAIL, VALID_PASSWORD);

    /** JwtRequest with invalid password. */
    public static final JwtRequest INVALID_PASSWORD_JWT_REQUEST = new JwtRequest(VALID_EMAIL, INVALID_PASSWORD);

    /** Message - invalid email or password. */
    public static final String INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE = "Invalid email or password";

    /** Authentication URI. */
    public static final String AUTHENTICATION_URI = "/api/v1/auth/login";

    /** URI to obtain new refresh and access tokens. */
    public static final String REFRESH_URI = "/api/v1/auth/refresh";

    /** URI for userAuthorization in userService. */
    public static final String USER_AUTH_URI = "/user/userAuthentication";

    /** URI for findByEmail in userService. */
    public static final String USER_FIND_URI = "/user/findByEmail";

    /** Clients list URI. */
    public static final String CLIENTS_URI = "/api/v1/clientservice/clients";

    /** RefreshJwtRequest with invalid refresh token. */
    public static final String INVALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
            + "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ."
            + "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    public static final AuthenticationRequestDto VALID_AUTHENTICATION_REQUEST_DTO
        = new AuthenticationRequestDto(VALID_EMAIL, VALID_PASSWORD);

    public static final UserEmailRequestDto USER_EMAIL_REQUEST_DTO = new UserEmailRequestDto(VALID_EMAIL);

    /** DTO with user data. */
    public static final UserInfoResponseDto USER_INFO_RESPONSE_DTO = new UserInfoResponseDto(
        "ivanov@mail.ru",
        "Ivan",
        "Ivanov",
        "USER"
    );
}
