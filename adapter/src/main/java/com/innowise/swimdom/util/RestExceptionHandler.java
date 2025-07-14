package com.innowise.swimdom.util;

import com.innowise.swimdom.exception.AuthenticationException;
import com.innowise.swimdom.exception.UserNotFoundException;
import com.innowise.swimdom.openapi.model.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

/**
 * Global error handler.
 *
 * @author DimSelyutin
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * Error handler with 400 bad REQUEST status.
     *
     * @param exception Exception object.
     * @return Error information.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        BadRequestException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class,
        IllegalArgumentException.class})
    public ErrorDto handleBadRequestException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return getErrorDto(HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for {@link UserNotFoundException }.
     *
     * @param exception Exception object.
     * @return Error information.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public ErrorDto handleNotFoundException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return getErrorDto(HttpStatus.NOT_FOUND);
    }

    /**
     * Handler for {@link AuthenticationException }.
     *
     * @param exception Exception object.
     * @return Error information.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    public ErrorDto handleAuthenticationException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return getErrorDto(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Unexpected exception handler.
     *
     * @param exception Exception object.
     * @return Error information.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ErrorDto handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates the {@link ErrorDto} object for the specified HTTP status.
     *
     * @param httpStatus HTTP status
     * @return response-the object of the error message
     */
    private ErrorDto getErrorDto(HttpStatus httpStatus) {
        return new ErrorDto(httpStatus.value(), httpStatus.getReasonPhrase().toUpperCase(), LocalDateTime.now());
    }
}

