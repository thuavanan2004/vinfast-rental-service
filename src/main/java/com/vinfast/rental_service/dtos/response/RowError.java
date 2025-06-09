package com.vinfast.rental_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RowError {
    private int rowNumber;
    private String message;
}
