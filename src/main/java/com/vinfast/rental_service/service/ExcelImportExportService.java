package com.vinfast.rental_service.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExcelImportExportService<T> {
    ResponseEntity<T> exportData(List<T> data);
}
