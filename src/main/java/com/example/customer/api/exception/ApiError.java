package com.example.customer.api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception response class with details of raised exception
 * 
 * @author akshay
 *
 */
@Getter
@AllArgsConstructor
public class ApiError {

    private final HttpStatus status;
    private final LocalDateTime timestamp;
    private final String message;
}
