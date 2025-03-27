package com.vinfast.rental_service.modules.admin.application.dto.response;

import lombok.Data;

@Data
public class AdminAuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private AdminProfileResponse profile;
}
