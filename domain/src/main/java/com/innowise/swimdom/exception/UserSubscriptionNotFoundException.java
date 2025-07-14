package com.innowise.swimdom.exception;

/**
 * An exception indicating that the user sent wrong request.
 * Error code: 400.
 *
 */
public class UserSubscriptionNotFoundException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public UserSubscriptionNotFoundException(String message) {
        super(message);
    }
}
