package com.vinfast.rental_service.exceptions;

public class InvalidPeriodException extends RuntimeException {
    public InvalidPeriodException(String message) {
        super(message);
    }
}
