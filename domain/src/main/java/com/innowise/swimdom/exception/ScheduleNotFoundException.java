package com.innowise.swimdom.exception;

/**
 * An exception indicating that schedule not found for pool.
 * Error code: 404.
 */
public class ScheduleNotFoundException extends RuntimeException {

    /**
     * Constructor exception.
     *
     * @param message message
     */
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
