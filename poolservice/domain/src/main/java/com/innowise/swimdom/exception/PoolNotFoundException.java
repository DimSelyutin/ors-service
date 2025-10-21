package com.innowise.swimdom.exception;

/**
 * An exception indicating that the resource requested by the client was not found on the server.
 * Error code: 404.
 *
 */
public class PoolNotFoundException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public PoolNotFoundException(String message) {
        super(message);
    }
}
