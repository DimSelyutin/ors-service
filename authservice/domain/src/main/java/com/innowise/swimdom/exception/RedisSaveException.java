package com.innowise.swimdom.exception;

/**
 * Error saving data in Redis.
 */
public class RedisSaveException extends RuntimeException {
    public RedisSaveException() {
        super("Failed to save to Redis");
    }
}
