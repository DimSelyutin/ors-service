package com.innowise.swimdom.event;

import java.util.UUID;

/**
 * Event for creating pool.
 */
public record PoolCreatedEvent(
        UUID poolId,
        String name,
        String location){
}


