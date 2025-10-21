package com.innowise.swimdom.event;

import java.util.UUID;

/**
 * Event for update user.
 */
public record UserUpdatedEvent(
    UUID userId,
    String email,
    String role){

}

