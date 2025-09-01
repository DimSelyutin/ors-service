package com.innowise.swimdom.exception;

/**
 * Error occurring when provided refresh token does not match expected.
 */
public class IncorrectRefreshTokenException extends RuntimeException {
    public IncorrectRefreshTokenException() {
        super("Incorrect refresh token");
    }
}
