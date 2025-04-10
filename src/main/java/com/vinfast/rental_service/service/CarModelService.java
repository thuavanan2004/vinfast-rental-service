package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.CarAvailabilityRequest;
import com.vinfast.rental_service.dtos.request.CarModelCreateRequest;
import com.vinfast.rental_service.dtos.request.CarModelUpdateRequest;
import com.vinfast.rental_service.dtos.response.CarModelDetailResponse;
import com.vinfast.rental_service.dtos.response.CarModelResponse;
import com.vinfast.rental_service.dtos.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarModelService {
    void createCarModel(CarModelCreateRequest request);

    void updateCarModel(long carModelId, CarModelUpdateRequest request);

    CarModelDetailResponse getCarModel(long carModelId);

    List<CarModelDetailResponse> getListCarModel();

    List<CarModelResponse> getAvailableCarModels(CarAvailabilityRequest request);
}
