package com.innowise.swimdom.dto;

/**
 * DTO for retrieving user data from UserService.
 *
 * @param email email
 * @param firstname first name
 * @param lastname last name
 * @param access role
 */
public record UserInfoResponseDto(
    String email,
    String firstname,
    String lastname,
    String access
) {

}
