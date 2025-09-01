package com.innowise.swimdom.exception;

/**
 * Error - requested data not found.
 */

public class NoContentException extends RuntimeException {
    /**
     * Creates a new exception with a predefined message.
     */
    public NoContentException() {
        super("User not found");
    }
}
