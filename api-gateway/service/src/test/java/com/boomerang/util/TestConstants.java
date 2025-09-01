package com.innowise.swimdom.util;

import com.innowise.swimdom.dto.UserInfoResponseDto;
import com.innowise.swimdom.exception.UnauthorizedException;
import java.util.HashMap;
import java.util.Map;
import reactor.core.publisher.Mono;

/**
 * Утилитарный класс с константами для тестирования.
 */
public class TestConstants {

    /**
     * JWT access токен.
     */
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * JWT refresh токен.
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * Валидный логин.
     */
    public static final String VALID_EMAIL = "ivanov@mail.ru";

    /**
     * Валидный пароль.
     */
    public static final String VALID_PASSWORD = "qwerty";

    /**
     * Невалидный логин.
     */
    public static final String INVALID_EMAIL = "ivanov123@mail.ru";

    /**
     * Невалидный пароль.
     */
    public static final String INVALID_PASSWORD = "12345";

    /**
     * Сообщение - неверный пароль.
     */
    public static final String INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE = "Неверный email или пароль";

    public static final String RESOURCE_NOT_AVAILABLE_ERROR_MESSAGE = "Ресурс не доступен";

    /**
     * USER_INFO с данными пользователя.
     */

    public static final UserInfoResponseDto USER_INFO_RESPONSE_DTO = new UserInfoResponseDto(
        "ivanov@mail.ru",
        "Иван",
        "Иванов",
        "USER"
    );

    /**
     * HashMap с access и refresh токенами.
     */
    public static final Map<String, Object> TOKEN_MAP = new HashMap<>() {{
            put("accessToken", ACCESS_TOKEN);
            put("refreshToken", REFRESH_TOKEN);
        }};

    /**
     * Mono обертка HashMap с данными пользователя.
     */
    public static final Mono<UserInfoResponseDto> USER_INFO_MONO = Mono.just(USER_INFO_RESPONSE_DTO);

    public static final UnauthorizedException UNAUTHORIZED_EXCEPTION
        = new UnauthorizedException(INVALID_EMAIL_OR_PASSWORD_ERROR_MESSAGE){};

}
