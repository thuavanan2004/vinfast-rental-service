package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "name must be not blank")
    private String name;

    @NotBlank(message = "email must be not blank")
    @Email
    private String email;

    @NotBlank(message = "password must be not blank")
    private String password;

//    @PhoneNumber
    private String phone;

    private String address;
}

