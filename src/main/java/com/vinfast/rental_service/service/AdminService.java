package com.vinfast.rental_service.service;


import com.vinfast.rental_service.dtos.response.AdminDetailResponse;

public interface AdminService {
    AdminDetailResponse getAdminById(long id);
}
