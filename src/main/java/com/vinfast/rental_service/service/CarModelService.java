package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.CarModelCreateRequest;
import com.vinfast.rental_service.dtos.request.CarModelUpdateRequest;

public interface CarModelService {
    void createCarModel(CarModelCreateRequest request);

    void updateCarModel(long carModelId, CarModelUpdateRequest request);
}
