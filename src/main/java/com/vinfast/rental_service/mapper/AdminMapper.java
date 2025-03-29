package com.vinfast.rental_service.mapper;


import com.vinfast.rental_service.dtos.request.AdminCreateRequest;
import com.vinfast.rental_service.dtos.request.AdminUpdateRequest;
import com.vinfast.rental_service.dtos.response.AdminDetailResponse;
import com.vinfast.rental_service.dtos.response.AdminProfileResponse;
import com.vinfast.rental_service.model.Admin;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin toEntity(AdminCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminFromDto(AdminUpdateRequest dto, @MappingTarget Admin entity);

    AdminDetailResponse toDetailResponse(Admin admin);
    AdminProfileResponse toProfileResponse(Admin admin);
}

