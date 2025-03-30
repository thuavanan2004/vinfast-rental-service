package com.vinfast.rental_service.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String drivingLicenseNumber;
    private String drivingLicenseImage;
    private LocalDate dateOfBirth;
    private Boolean verified;
    private String status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
