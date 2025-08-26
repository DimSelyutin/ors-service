package com.innowise.swimdom.exception;

/**
 * An exception indicating that the user was not found.
 * Error code: 404.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
