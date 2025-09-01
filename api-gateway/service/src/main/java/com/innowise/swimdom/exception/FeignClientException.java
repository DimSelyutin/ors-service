package com.innowise.swimdom.exception;

/**
 * Feign Client error.
 */
public class FeignClientException extends RuntimeException {
    public FeignClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeignClientException(String message) {
        super(message);
    }
}
