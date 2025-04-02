package com.vinfast.rental_service.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
    private boolean systemRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
