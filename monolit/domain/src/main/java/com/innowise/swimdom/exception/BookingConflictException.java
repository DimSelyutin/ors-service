package com.innowise.swimdom.exception;

/**
 * Exception thrown when there is a conflict with booking operations.
 */
public class BookingConflictException extends RuntimeException {

    public BookingConflictException(String message) {
        super(message);
    }

    public BookingConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
