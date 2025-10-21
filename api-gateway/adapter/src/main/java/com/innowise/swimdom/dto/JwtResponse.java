package com.innowise.swimdom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO response with JWT tokens.
 *
 * @param accessToken  access token
 * @param refreshToken refresh token
 */
@Schema(description = "Response message after successful authentication containing tokens")
public record JwtResponse(
    String accessToken,
    String refreshToken) {

}
