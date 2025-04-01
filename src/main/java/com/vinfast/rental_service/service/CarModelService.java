package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.CarModelCreateRequest;
import com.vinfast.rental_service.dtos.request.CarModelUpdateRequest;
import com.vinfast.rental_service.dtos.response.CarModelResponse;

import java.util.List;

public interface CarModelService {
    void createCarModel(CarModelCreateRequest request);

    void updateCarModel(long carModelId, CarModelUpdateRequest request);

    CarModelResponse getCarModel(long carModelId);

    List<CarModelResponse> getListCarModel();
}
