package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoleRequest {

    @NotBlank(message = "Role name cannot be blank")
    @Size(min = 3, max = 50, message = "Role name must be between 3-50 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private boolean systemRole;
}