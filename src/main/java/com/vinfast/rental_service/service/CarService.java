package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.CarCreateRequest;
import com.vinfast.rental_service.dtos.request.CarUpdateRequest;
import com.vinfast.rental_service.dtos.request.MaintenanceRequest;
import com.vinfast.rental_service.dtos.response.CarResponse;
import com.vinfast.rental_service.dtos.response.MaintenanceDetailResponse;
import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.enums.CarStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CarService {
    void addNewCar(long carModelId, CarCreateRequest request);

    CarResponse getInfoCar(long carId);

    void updateCarStatus(long carId, CarStatus status);

    void updateCar(long carId, CarUpdateRequest request);

    void createMaintenance(long carId, MaintenanceRequest request);

    MaintenanceDetailResponse maintenanceReport(long maintenanceId);

    PageResponse<?> maintenanceReportsByCarId(long carId, Pageable pageable);

    PageResponse<?> getListCarByCarModel(long carModelId, Pageable pageable);

    void exportCars() throws IOException;

    void importCars(MultipartFile file) throws IOException;
}
