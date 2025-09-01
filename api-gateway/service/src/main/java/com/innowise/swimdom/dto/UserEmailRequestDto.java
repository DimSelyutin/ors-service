package com.innowise.swimdom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for requesting user data by email in UserService.
 *
 * @param email login
 */
@Schema(description = "Information to request authentication on the user service side in case of token refresh")
public record UserEmailRequestDto(String email) {

}
