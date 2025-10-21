package com.innowise.swimdom.exception;

/**
 * Access rights error.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
