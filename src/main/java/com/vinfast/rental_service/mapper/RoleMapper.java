package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.request.RoleRequest;
import com.vinfast.rental_service.dtos.response.RoleResponse;
import com.vinfast.rental_service.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleRequest request);

    RoleResponse toDTO(Role role);
}
