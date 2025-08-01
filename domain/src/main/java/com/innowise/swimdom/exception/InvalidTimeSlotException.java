package com.innowise.swimdom.exception;

/**
 * Exception thrown when the time slot is invalid.
 */
public class InvalidTimeSlotException extends RuntimeException {

    public InvalidTimeSlotException(String message) {
        super(message);
    }

    public InvalidTimeSlotException(String message, Throwable cause) {
        super(message, cause);
    }
}
