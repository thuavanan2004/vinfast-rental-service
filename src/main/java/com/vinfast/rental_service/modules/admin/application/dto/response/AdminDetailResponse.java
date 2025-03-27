package com.vinfast.rental_service.modules.admin.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vinfast.rental_service.modules.admin.domain.enums.AdminRole;
import com.vinfast.rental_service.modules.admin.domain.enums.AdminStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDetailResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private AdminRole role;
    private AdminStatus status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
