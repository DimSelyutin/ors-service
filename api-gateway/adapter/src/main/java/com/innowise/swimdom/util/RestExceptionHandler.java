package com.innowise.swimdom.util;

import com.innowise.swimdom.dto.ErrorDto;
import com.innowise.swimdom.exception.BadRequestException;
import com.innowise.swimdom.exception.FeignClientException;
import com.innowise.swimdom.exception.ForbiddenException;
import com.innowise.swimdom.exception.IncorrectRefreshTokenException;
import com.innowise.swimdom.exception.NoContentException;
import com.innowise.swimdom.exception.NotFoundException;
import com.innowise.swimdom.exception.RedisSaveException;
import com.innowise.swimdom.exception.ServiceUnavailableException;
import com.innowise.swimdom.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global error handler.
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handler for {@link BadRequestException} and {@link NotFoundException}.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class, NotFoundException.class})
    public ErrorDto handleBadRequestException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(400, "Invalid username or password", LocalDateTime.now());
    }

    /**
     * Handler {@link ServiceUnavailableException}.
     */
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler({ServiceUnavailableException.class})
    public ErrorDto handleServiceUnavailableException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(503, ex.getMessage(), LocalDateTime.now());
    }

    /**
     * Handler for {@link UnauthorizedException}.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class, IncorrectRefreshTokenException.class})
    public ErrorDto handleUnauthorizedException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(401, ex.getMessage(), LocalDateTime.now());
    }

    /**
     * Handler {@link ForbiddenException}.
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorDto handlerForbiddenException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(403, ex.getMessage(), LocalDateTime.now());
    }

    /**
     * Handler {@link FeignClientException}.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({FeignClientException.class, RedisSaveException.class, Exception.class})
    public ErrorDto handlerFeignClientException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto(500, ex.getMessage(), LocalDateTime.now());
    }

    /**
     * Handler {@link FeignClientException}.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoContentException.class)
    public ErrorDto handleNoContentException(Exception ex) {

        return new ErrorDto(204, ex.getMessage(), LocalDateTime.now());
    }
}
