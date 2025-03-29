package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.response.UserResponse;
import com.vinfast.rental_service.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDTO(User user);
}
