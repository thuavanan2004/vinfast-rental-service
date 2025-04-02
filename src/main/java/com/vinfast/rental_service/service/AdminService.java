package com.vinfast.rental_service.service;


import com.vinfast.rental_service.dtos.request.AdminCreateRequest;
import com.vinfast.rental_service.dtos.request.AdminLoginRequest;
import com.vinfast.rental_service.dtos.request.AdminUpdateRequest;
import com.vinfast.rental_service.dtos.request.RoleRequest;
import com.vinfast.rental_service.dtos.response.AdminAuthResponse;
import com.vinfast.rental_service.dtos.response.AdminDetailResponse;
import com.vinfast.rental_service.dtos.response.PermissionsResponse;
import com.vinfast.rental_service.dtos.response.RoleResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AdminService {
    AdminDetailResponse getAdminById(long id);

    AdminAuthResponse login(AdminLoginRequest request);

    void logout(HttpServletRequest request);

    void create(AdminCreateRequest request);

    void update(AdminUpdateRequest request, long adminId);

    List<RoleResponse> getRoles();

    void createRole(RoleRequest request);

    List<PermissionsResponse> getPermissionsByRoleId(long roleId);

    List<PermissionsResponse> getPermissions();
}
