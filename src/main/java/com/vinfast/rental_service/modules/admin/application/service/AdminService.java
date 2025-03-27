package com.vinfast.rental_service.modules.admin.application.service;

import com.vinfast.rental_service.modules.admin.application.dto.response.AdminDetailResponse;
import com.vinfast.rental_service.modules.admin.domain.entity.Admin;

public interface AdminService {
    AdminDetailResponse getAdminById(long id);
}
