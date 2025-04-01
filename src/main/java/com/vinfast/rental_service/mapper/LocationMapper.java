package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.request.LocationRequest;
import com.vinfast.rental_service.dtos.response.LocationResponse;
import com.vinfast.rental_service.model.PickupLocation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface LocationMapper {
    PickupLocation toEntity(LocationRequest request);

    LocationResponse toDTO(PickupLocation location);

    void updateLocation(LocationRequest request, @MappingTarget PickupLocation location);
}
