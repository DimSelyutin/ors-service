package com.innowise.swimdom.event;

import java.util.UUID;

public record PoolCreatedEvent(
     UUID poolId,
     String name,
     String location
     ){}


