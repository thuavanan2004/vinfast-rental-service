package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUpdateRequest {
    @Size(min = 5, max = 50, message = "Username must be between 5-50 characters")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters with letters and numbers"
    )
    private String password;

    private String fullName;
    private String phone;
    private String status; // "active", "inactive", etc.
}
