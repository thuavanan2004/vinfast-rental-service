package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.CarCreateRequest;
import com.vinfast.rental_service.dtos.request.CarUpdateRequest;
import com.vinfast.rental_service.dtos.response.CarResponse;
import com.vinfast.rental_service.enums.CarStatus;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.CarMapper;
import com.vinfast.rental_service.model.Car;
import com.vinfast.rental_service.model.CarModel;
import com.vinfast.rental_service.model.PickupLocation;
import com.vinfast.rental_service.repository.CarModelRepository;
import com.vinfast.rental_service.repository.CarRepository;
import com.vinfast.rental_service.repository.PickupLocationRepository;
import com.vinfast.rental_service.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarModelRepository carModelRepository;

    private final CarRepository carRepository;

    private final PickupLocationRepository pickupLocationRepository;

    private final CarMapper carMapper;

    @Override
    public void addNewCar(long carModelId, CarCreateRequest request) {
        CarModel carModel = carModelRepository.findById(carModelId).orElseThrow(() -> new ResourceNotFoundException("Car model not found"));
        if (carRepository.existsByLicensePlateOrVinNumber(request.getLicensePlate(), request.getVinNumber())) {
            throw new InvalidDataException("License plate or VIN number already exists");
        }
        Car car = carMapper.toEntity(request);
        car.setCarModel(carModel);
        Optional.ofNullable(request.getPickupLocationId())
                .ifPresent(locationId -> {
                    PickupLocation pickupLocation = pickupLocationRepository.findById(locationId)
                            .orElseThrow(() -> new ResourceNotFoundException("Pickup location not found with id: " + locationId));
                    car.setPickupLocation(pickupLocation);
                });
        carRepository.save(car);

        log.info("Add new car to the system successfully");
    }

    @Override
    public CarResponse getInfoCar(long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        return carMapper.toDTO(car);
    }

    @Override
    public void updateCarStatus(long carId, CarStatus status) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        if (car.getStatus() == status) {
            throw new InvalidDataException("Car already has status: " + status);
        }
        car.setStatus(status);
        carRepository.save(car);

        log.info("Updated car ID {} status from {} to {}",
                carId, car.getStatus(), status);
    }

    @Override
    public void updateCar(long carId, CarUpdateRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        if (carRepository.existsByLicensePlateAndIdNot(request.getLicensePlate(), carId)) {
            throw new InvalidDataException("License plate " + request.getLicensePlate() + " already exists on another car");
        }

        if (carRepository.existsByVinNumberAndIdNot(request.getVinNumber(), carId)) {
            throw new InvalidDataException("VIN number " + request.getVinNumber() + " already exists on another car");
        }
        CarModel carModel = carModelRepository.findById(request.getCarModelId())
                .orElseThrow(() -> new ResourceNotFoundException("Car model not found with id: " + request.getCarModelId()));

        carMapper.updateCar(request, car);
        car.setCarModel(carModel);

        Optional.ofNullable(request.getPickupLocationId()).ifPresent(locationId -> {
            PickupLocation pickupLocation = pickupLocationRepository.findById(locationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pickup location not found with id: " + locationId));
            car.setPickupLocation(pickupLocation);
        });

        carRepository.save(car);

        log.info("Updated car ID {} with license plate {}", carId, request.getLicensePlate());
    }
}
