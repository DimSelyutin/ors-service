package com.innowise.swimdom.mapper;

import com.innowise.swimdom.dto.JwtResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Map;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Mapper for converting a map with JWT tokens to JwtResponse DTO.
 */
@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface JwtResponseMapper {

    /**
     * Maps a map containing "accessToken" and "refreshToken" to JwtResponse.
     *
     * @param tokenMap source map
     * @return JwtResponse or null when source is null
     */
    @Mapping(target = "accessToken", expression = "java((String) tokenMap.get(\"accessToken\"))")
    @Mapping(target = "refreshToken", expression = "java((String) tokenMap.get(\"refreshToken\"))")
    JwtResponse mapToDto(Map<String, Object> tokenMap);
}


