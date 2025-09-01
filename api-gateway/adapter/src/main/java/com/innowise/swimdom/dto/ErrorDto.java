package com.innowise.swimdom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Error DTO.
 *
 * @param errorCode error code
 * @param errorMessage message
 * @param timestamp time
 */
@Schema(description = "System response message in case of error")
public record ErrorDto(
        int errorCode,
        String errorMessage,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp
) {
}
