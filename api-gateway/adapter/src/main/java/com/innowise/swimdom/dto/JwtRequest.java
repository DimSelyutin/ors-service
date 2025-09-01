package com.innowise.swimdom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JWT token request DTO.
 *
 * @param email    email
 * @param password password
 */
@Schema(description = "Information for employee authentication")
public record JwtRequest(
    String email,
    String password) {

}
