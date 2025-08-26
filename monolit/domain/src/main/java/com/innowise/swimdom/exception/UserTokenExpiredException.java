package com.innowise.swimdom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that token expired.
 * Error code: 401.
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserTokenExpiredException extends RuntimeException {
    public UserTokenExpiredException(String message) {
        super(message);
    }

}
