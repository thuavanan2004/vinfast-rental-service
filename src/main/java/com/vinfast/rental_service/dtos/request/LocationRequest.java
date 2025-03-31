package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LocationRequest {
    @NotBlank(message = "name must be not blank")
    private String name;

    @NotBlank(message = "address must be not blank")
    private String address;

    @NotBlank(message = "city must be not blank")
    private String city;

    @NotBlank(message = "district must be not blank")
    private String district;

    @NotBlank(message = "contactPhone must be not blank")
    private String contactPhone;

    private Double latitude;
    private Double longitude;

    private String operatingHours;
    private String description;
}
