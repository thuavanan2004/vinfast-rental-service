package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.CarAvailabilityRequest;
import com.vinfast.rental_service.dtos.request.CarModelCreateRequest;
import com.vinfast.rental_service.dtos.request.CarModelUpdateRequest;
import com.vinfast.rental_service.dtos.response.CarModelDetailResponse;
import com.vinfast.rental_service.dtos.response.CarModelResponse;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.CarModelMapper;
import com.vinfast.rental_service.model.CarImage;
import com.vinfast.rental_service.model.CarModel;
import com.vinfast.rental_service.repository.CarImageRepository;
import com.vinfast.rental_service.repository.CarModelRepository;
import com.vinfast.rental_service.repository.projections.CarModelProjection;
import com.vinfast.rental_service.service.CarModelService;
import com.vinfast.rental_service.service.common.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarModelServiceImpl implements CarModelService {

    private final CloudinaryService cloudinaryService;

    private final CarModelRepository carModelRepository;

    private final CarImageRepository carImageRepository;

    private final CarModelMapper carModelMapper;

    public CarModel getCarModelById(long carModelId){
        return carModelRepository.findById(carModelId).orElseThrow(() -> new ResourceNotFoundException("Car model not found"));
    }

    @Override
    @Transactional
    public void createCarModel(CarModelCreateRequest request) {
        if (carModelRepository.existsByModelCode(request.getModelCode())) {
            throw new IllegalArgumentException("Model code already exists");
        }

        CarModel carModel = carModelMapper.toEntity(request);
        carModelRepository.save(carModel);

        List<String> urls = cloudinaryService.uploadFiles(request.getImages());

        List<CarImage> carImages = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            CarImage carImage = new CarImage();
            carImage.setImageUrl(urls.get(i));
            carImage.setCarModel(carModel);

            if (i == request.getPrimaryImageIndex()) {
                carImage.setIsPrimary(true);
            } else {
                carImage.setDisplayOrder(i);
            }
            carImages.add(carImage);
        }

        carImageRepository.saveAll(carImages);
        log.info("Service: Create car-model successfully");
    }

    @Override
    public void updateCarModel(long carModelId, CarModelUpdateRequest request) {
        CarModel carModel = getCarModelById(carModelId);
        carModelMapper.updateCarModel(request, carModel);

        carModelRepository.save(carModel);
        log.info("Service: Update car-model successfully");
    }

    @Override
    public CarModelDetailResponse getCarModel(long carModelId) {
        CarModel carModel = getCarModelById(carModelId);

        return carModelMapper.toDTO(carModel);
    }

    @Override
    public List<CarModelDetailResponse> getListCarModel() {
        List<CarModel> records = carModelRepository.findAll();
        return records.stream().map(carModelMapper::toDTO).toList();
    }

    @Override
    public List<CarModelResponse> getAvailableCarModels(CarAvailabilityRequest request) {
        List<CarModelProjection> projections = carModelRepository
                .findAvailableModels(request.getCity(), request.getPickupTime(), request.getReturnTime(), request.getRentalType());

        List<CarModelResponse> list = projections.stream()
                .map(p -> CarModelResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .modelCode(p.getModelCode())
                        .mainImage(p.getMainImage())
                        .rangePerCharge(p.getRangePerCharge())
                        .seatingCapacity(p.getSeatingCapacity())
                        .trunkCapacity(p.getTrunkCapacity())
                        .basePrice(p.getBasePrice())
                        .vehicleType(p.getVehicleType())
                        .available(p.getAvailable() != null && p.getAvailable() == 1)
                        .build()).collect(Collectors.toList());


       return list;
    }

}
