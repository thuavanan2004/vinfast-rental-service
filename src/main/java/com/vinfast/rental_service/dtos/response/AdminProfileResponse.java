package com.vinfast.rental_service.dtos.response;

import lombok.Data;

@Data
public class AdminProfileResponse {
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String status;
}
