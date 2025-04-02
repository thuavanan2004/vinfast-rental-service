package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.response.PermissionsResponse;
import com.vinfast.rental_service.model.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionsResponse toDTO(Permission permission);
}
