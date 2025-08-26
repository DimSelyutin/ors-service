package com.innowise.swimdom.exception;

/**
 * Error that occurs when the sent refresh token does not match the expected one.
 */
public class IncorrectRefreshTokenException extends RuntimeException {

    /**
     * Constructor exception.
     *
     */
    public IncorrectRefreshTokenException() {
        super("Incorrect refresh token");
    }
}
