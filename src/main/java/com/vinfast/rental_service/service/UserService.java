package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.UserResponse;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.enums.UserStatus;
import com.vinfast.rental_service.model.User;
import org.springframework.data.domain.Pageable;

public interface UserService {
    PageResponse<?> getAll(Pageable pageable, String[] users);

    User getUserByEmail(String email);

    UserResponse getUser(long userId);

    void changeStatus(long userId, UserStatus status);

    PageResponse<?> rentalHistory(long userId, Pageable pageable);

    boolean existsByEmail(String email);
}
