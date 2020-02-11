package com.rabo.assignment.customer.api.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rabo.assignment.customer.api.exception.ApiError;
import com.rabo.assignment.customer.api.exception.CustomerNotFoundException;

/**
 * Common exception handler for application, to handle exception raised for any
 * API endpoint.
 * 
 * @see ResponseEntityExceptionHandler
 * @author akshay
 *
 */
@RestControllerAdvice
public class CustomerApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Exception handler for {@link CustomerNotFoundException} with proper json
     * structure to show error.
     * 
     * @param exception
     *            {@link RuntimeException} to be handled.
     * @return {@link ApiError} as Json with error message and status code.
     */
    @ExceptionHandler(value = CustomerNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(RuntimeException exception) {
        return new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), exception.getMessage());
    }
}
