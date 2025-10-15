package com.innowise.swimdom.event;

import jakarta.validation.constraints.Email;

import java.util.UUID;

public record UserCreatedEvent(
        UUID userId,
        @Email(message = "Email should be valid")
        String email,
        String role) {
}
