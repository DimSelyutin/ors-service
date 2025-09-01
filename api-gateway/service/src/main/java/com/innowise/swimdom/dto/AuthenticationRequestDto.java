package com.innowise.swimdom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for authentication request to UserService.
 *
 * @param email    login
 * @param password password
 */
@Schema(description = "Information for requesting authentication on the user service side")
public record AuthenticationRequestDto(
    String email,
    String password) {

}
