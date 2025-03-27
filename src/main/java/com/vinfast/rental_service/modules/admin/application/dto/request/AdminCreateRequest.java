package com.vinfast.rental_service.modules.admin.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
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

    @NotBlank(message = "Role cannot be blank")
    private String role;

    private String fullName;
    private String phone;
}
