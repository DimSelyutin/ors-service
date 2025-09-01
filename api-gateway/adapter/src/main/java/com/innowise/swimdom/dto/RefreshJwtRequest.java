package com.innowise.swimdom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO with refresh token to obtain a new access token.
 *
 * @param refreshToken refresh token
 */
@Schema(description = "Information to perform token refresh")
public record RefreshJwtRequest(
    String refreshToken) {

}
