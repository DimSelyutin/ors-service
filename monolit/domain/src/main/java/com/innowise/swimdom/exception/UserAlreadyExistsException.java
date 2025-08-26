package com.innowise.swimdom.exception;

/**
 * An exception indicating that user already exists.
 * Error code: 400.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
