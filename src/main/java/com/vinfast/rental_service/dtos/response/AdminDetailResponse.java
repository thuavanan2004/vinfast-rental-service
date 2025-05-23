package com.vinfast.rental_service.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.vinfast.rental_service.enums.AdminStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDetailResponse {
    private Long id;
    private Long roleId;
    private String roleName;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private AdminStatus status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
