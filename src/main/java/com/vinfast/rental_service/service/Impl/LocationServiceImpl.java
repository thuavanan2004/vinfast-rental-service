package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.LocationRequest;
import com.vinfast.rental_service.enums.PickupLocationStatus;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.LocationMapper;
import com.vinfast.rental_service.model.PickupLocation;
import com.vinfast.rental_service.repository.LocationRepository;
import com.vinfast.rental_service.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    @Override
    public void createLocation(LocationRequest request) {
        PickupLocation location = locationMapper.toEntity(request);

        locationRepository.save(location);

        log.info("Create new location successfully");
    }

    @Override
    public void updateLocation(long locationId, LocationRequest request) {
        PickupLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));

        locationMapper.updateLocation(request, location);

        locationRepository.save(location);
        log.info("Update location successfully");
    }

    @Override
    public void changeStatus(long locationId, PickupLocationStatus status) {
        PickupLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));

        if(location.getPickupLocationStatus() == status){
            throw new InvalidDataException("Status already exists");
        }
        location.setPickupLocationStatus(status);

        locationRepository.save(location);
        log.info("Change status location successfully");
    }
}
