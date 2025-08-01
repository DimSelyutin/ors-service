package com.innowise.swimdom.exception;

/**
 * An exception indicating that the resource requested by the client was not found on the server.
 * Error code: 400.
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
