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
    @NotBlank(message = "Email cannot be blank.")
    @Size(max = 100, message = "Email length cannot exceed 100 characters.")
    @Email(message = "Email must be a valid email address.")
    String email,

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters.")
    String password) {

}