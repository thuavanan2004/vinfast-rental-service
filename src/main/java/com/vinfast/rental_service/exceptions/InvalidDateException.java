package com.vinfast.rental_service.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidDateException extends RuntimeException {
    private final String errorCode;
    public InvalidDateException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
