package com.rabo.assignment.customer.api.exception.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
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

    private static final Logger LOG = LoggerFactory.getLogger(CustomerApiExceptionHandler.class);

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
    public ApiError handleNotFoundException(CustomerNotFoundException exception) {
        LOG.error("Customer not found exception {}", exception);
        return new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), exception.getMessage());
    }

    /**
     * Method overridden to add custom error messages
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        LOG.error("Validation Error {}", ex);
        List<ApiError> errorList = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> {
            return new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), fieldError.getDefaultMessage());
        }).collect(Collectors.toList());
        return handleExceptionInternal(ex, errorList, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Generic exception handler for all {@link Exception} clas
     * 
     * @param ex
     *            {@link Exception} to be handled
     * @return generic api error response.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError genericExceptionHandler(Exception ex) {
        LOG.error("Exception in application {}", ex);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), "Internal Server ERROR");
    }
}
