package com.vinfast.rental_service.dtos.response;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        String drivingLicenseNumber,
        String drivingLicenseImage,
        LocalDate dateOfBirth,
        Boolean verified,
        String status,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
