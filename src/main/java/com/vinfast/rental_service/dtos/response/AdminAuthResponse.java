package com.vinfast.rental_service.dtos.response;

import lombok.Data;

@Data
public class AdminAuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private AdminProfileResponse profile;
}
