package com.vinfast.rental_service.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminAuthResponse {
    private String accessToken;
    private Long expiresIn;
    private AdminProfileResponse profile;
}
