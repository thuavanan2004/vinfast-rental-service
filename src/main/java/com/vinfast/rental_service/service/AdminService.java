package com.vinfast.rental_service.service;


import com.vinfast.rental_service.dtos.request.AdminLoginRequest;
import com.vinfast.rental_service.dtos.response.AdminAuthResponse;
import com.vinfast.rental_service.dtos.response.AdminDetailResponse;
import com.vinfast.rental_service.dtos.response.PageResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AdminService {
    AdminDetailResponse getAdminById(long id);

    AdminAuthResponse login(AdminLoginRequest request);

    void logout(HttpServletRequest request);

}
