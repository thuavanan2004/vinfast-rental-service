package com.vinfast.rental_service.mapper;


import com.vinfast.rental_service.dtos.request.AdminCreateRequest;
import com.vinfast.rental_service.dtos.request.AdminUpdateRequest;
import com.vinfast.rental_service.dtos.response.AdminDetailResponse;
import com.vinfast.rental_service.dtos.response.AdminProfileResponse;
import com.vinfast.rental_service.model.Admin;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin toEntity(AdminCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminFromDto(AdminUpdateRequest dto, @MappingTarget Admin entity);

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    AdminDetailResponse toDetailResponse(Admin admin);

    AdminProfileResponse toProfileResponse(Admin admin);
}

