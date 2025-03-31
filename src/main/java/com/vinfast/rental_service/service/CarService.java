package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.CarCreateRequest;
import com.vinfast.rental_service.dtos.request.CarUpdateRequest;
import com.vinfast.rental_service.dtos.response.CarResponse;
import com.vinfast.rental_service.enums.CarStatus;

public interface CarService {
    void addNewCar(long carModelId, CarCreateRequest request);

    CarResponse getInfoCar(long carId);

    void updateCarStatus(long carId, CarStatus status);

    void updateCar(long carId, CarUpdateRequest request);
}
