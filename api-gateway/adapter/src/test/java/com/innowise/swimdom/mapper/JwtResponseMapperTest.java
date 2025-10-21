package com.innowise.swimdom.mapper;

import com.innowise.swimdom.dto.JwtResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class JwtResponseMapperTest {

    private final JwtResponseMapper mapper = Mappers.getMapper(JwtResponseMapper.class);

    @Test
    void mapToDto_validMap_returnsCorrectDto() {
        // Given
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);

        // When
        JwtResponse result = mapper.mapToDto(tokenMap);

        // Then
        assertNotNull(result, "Resulting DTO should not be null");
        assertEquals(accessToken, result.accessToken(), "Access token should be mapped correctly");
        assertEquals(refreshToken, result.refreshToken(), "Refresh token should be mapped correctly");
    }

    @Test
    void mapToDto_nullMap_returnsNull() {
        // Given
        Map<String, Object> tokenMap = null;

        // When
        JwtResponse result = mapper.mapToDto(tokenMap);

        // Then
        assertNull(result, "Resulting DTO should be null when input is null");
    }

    @Test
    void mapToDto_mapWithMissingKeys_returnsDtoWithNullFields() {
        // Given
        String refreshToken = "test-refresh-token";
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("refreshToken", refreshToken);
        // "accessToken" is missing

        // When
        JwtResponse result = mapper.mapToDto(tokenMap);

        // Then
        assertNotNull(result, "Resulting DTO should not be null");
        assertNull(result.accessToken(), "Missing access token should result in a null field");
        assertEquals(refreshToken, result.refreshToken(), "Refresh token should be mapped correctly");
    }

    @Test
    void mapToDto_emptyMap_returnsDtoWithNullFields() {
        // Given
        Map<String, Object> tokenMap = new HashMap<>();

        // When
        JwtResponse result = mapper.mapToDto(tokenMap);

        // Then
        assertNotNull(result, "Resulting DTO should not be null");
        assertNull(result.accessToken(), "Empty map should result in a null access token field");
        assertNull(result.refreshToken(), "Empty map should result in a null refresh token field");
    }
}
