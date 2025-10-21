package com.innowise.swimdom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for requesting user data by email in UserService.
 *
 * @param email login
 */
@Schema(description = "Information to request authentication on the user service side in case of token refresh")
public record UserEmailRequestDto(
    @NotBlank(message = "Email cannot be blank.")
    @Size(max = 100, message = "Email length cannot exceed 100 characters.")
    @Email(message = "Email must be a valid email address.")
    String email
) {

}
