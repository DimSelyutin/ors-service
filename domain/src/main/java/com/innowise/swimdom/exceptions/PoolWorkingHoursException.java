package com.innowise.swimdom.exceptions;

/**
 * An exception indicating that the user sent wrong request.
 * Error code: 400.
 *
 */
public class PoolWorkingHoursException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public PoolWorkingHoursException(String message) {
        super(message);
    }
}
