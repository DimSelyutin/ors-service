package com.innowise.swimdom.exception;

/**
 * An exception indicating that the user sent wrong request.
 * Error code: 400.
 *
 */
public class SubscriptionNotFoundException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
