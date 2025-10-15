package com.innowise.swimdom.event;

import java.util.UUID;

public record UserUpdatedEvent(
    UUID userId,
    String email,
    String role){}

