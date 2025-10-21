package com.innowise.swimdom.event;

import java.util.UUID;

/**
 * Event for deleted user.
 */
public record UserDeletedEvent(UUID userId) {}


