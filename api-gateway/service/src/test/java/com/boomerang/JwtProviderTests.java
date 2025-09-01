package com.innowise.swimdom;

import com.innowise.swimdom.service.JwtProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Properties;

import static com.innowise.swimdom.util.TestConstants.USER_INFO_RESPONSE_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тест проверяет корректность методов класса JwtProvider.
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtProviderTests {

    private JwtProvider jwtProvider;

    /**
     * Настройка JwtProvider через проперти из application.yml.
     */
    @BeforeAll
    public void setUp() {
        Properties prop = loadPropertiesFromFile("application.yml");

        String jwtAccessSecret = prop.get("access").toString();
        String jwtRefreshSecret = prop.get("refresh").toString();
        int accessExpirationMinutes = Integer.decode(prop.get("accessExpirationMinutes").toString());
        int refreshExpirationDays = Integer.decode(prop.get("refreshExpirationDays").toString());

        this.jwtProvider = new JwtProvider(
            jwtAccessSecret, jwtRefreshSecret, accessExpirationMinutes, refreshExpirationDays
        );
    }

    /**
     * Загрузка пропертей из application.yml.
     *
     * @param fileName путь к файлу
     * @return проперти
     */
    private Properties loadPropertiesFromFile(String fileName) {
        Properties prop = new Properties();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream stream = classLoader.getResourceAsStream(fileName);
            prop.load(stream);
            assert stream != null;
            stream.close();
        } catch (Exception e) {
            String msg = String.format("Failed to load file '%s' - %s - %s", fileName, e.getClass().getName(),
                e.getMessage());
            Assertions.fail(msg);
        }
        return prop;
    }

    @Test
    public void generateAccessTokenTest() {
        String token = jwtProvider.generateAccessToken(USER_INFO_RESPONSE_DTO);
        Claims claims = jwtProvider.getAccessClaims(token);
        assertNotNull(claims);
        assertEquals(USER_INFO_RESPONSE_DTO.email(), claims.getSubject());
        assertEquals(USER_INFO_RESPONSE_DTO.firstname(), claims.get("firstName"));
        assertEquals(USER_INFO_RESPONSE_DTO.lastname(), claims.get("lastName"));
        assertEquals(USER_INFO_RESPONSE_DTO.access(), claims.get("access"));
    }

    @Test
    public void generateRefreshTokenTest() {
        String token = jwtProvider.generateRefreshToken(USER_INFO_RESPONSE_DTO);
        Claims claims = jwtProvider.getRefreshClaims(token);
        assertNotNull(claims);
        assertEquals(USER_INFO_RESPONSE_DTO.email(), claims.getSubject());
    }
}
