package com.innowise.swimdom.exception;

/**
 * Error while saving data to Redis.
 */
public class RedisSaveException extends RuntimeException {
    public RedisSaveException() {
        super("Failed to save to Redis");
    }
}
