package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AssignPermissionsRequest {
    @NotEmpty(message = "permissionIds must not empty")
    private List<Long> permissionIds;
}
