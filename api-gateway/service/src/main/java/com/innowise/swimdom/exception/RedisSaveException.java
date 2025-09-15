package com.innowise.swimdom.exception;

/**
 * Error while saving data to Redis.
 */
public class RedisSaveException extends RuntimeException {

    public RedisSaveException(String message) {
        super(message);
    }

    public RedisSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
