package com.rabo.assignment.customer.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Customer Not Found")
public class CustomerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8368925973188672774L;

    /**
     * @param message
     * @param cause
     */
    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public CustomerNotFoundException(Throwable cause) {
        super(cause);
    }
}
