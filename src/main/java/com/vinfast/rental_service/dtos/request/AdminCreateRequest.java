package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AdminCreateRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, max = 50, message = "Username must be between 5-50 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters with letters and numbers"
    )
    private String password;

    private String fullName;
    private String phone;

    @NotNull(message = "roleId must be null")
    @Min(1)
    private long roleId;
}
