package com.vinfast.rental_service.dtos.response;

import lombok.Data;

@Data
public class LocationResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String district;
    private String contactPhone;
    private Double latitude;
    private Double longitude;
    private String operatingHours;
    private String description;
    private String status;
}
