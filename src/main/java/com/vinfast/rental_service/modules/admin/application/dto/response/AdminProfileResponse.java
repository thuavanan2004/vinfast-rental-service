package com.vinfast.rental_service.modules.admin.application.dto.response;

import lombok.Data;

@Data
public class AdminProfileResponse {
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private String status;
}
