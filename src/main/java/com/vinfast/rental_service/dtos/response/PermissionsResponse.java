package com.vinfast.rental_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PermissionsResponse {
    private long id;
    private String code;
    private String name;
    private String description;
    private String module;
}
