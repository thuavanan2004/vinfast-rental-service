package com.vinfast.rental_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ImportResult {
    private int totalRows;
    private int successCount;
    private List<RowError> errors;
}
