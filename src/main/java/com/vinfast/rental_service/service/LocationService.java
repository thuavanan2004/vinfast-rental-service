package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.LocationRequest;
import com.vinfast.rental_service.dtos.response.LocationResponse;
import com.vinfast.rental_service.enums.PickupLocationStatus;

import java.util.List;

public interface LocationService {
    void createLocation(LocationRequest request);

    void updateLocation(long locationId, LocationRequest request);

    void changeStatus(long locationId, PickupLocationStatus status);

    List<LocationResponse> getListLocation();

    LocationResponse getLocationById(long locationId);
}
