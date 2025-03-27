package com.vinfast.rental_service.modules.admin.infrastructure.controller;

import com.vinfast.rental_service.modules.admin.application.dto.response.AdminDetailResponse;
import com.vinfast.rental_service.modules.admin.application.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

//    @PostMapping
//    public ResponseEntity<AdminDetailResponse> createAdmin(
//            @Valid @RequestBody AdminCreateRequest request
//    ) {
//        Admin admin = adminService.createAdmin(request);
//        return ResponseEntity.ok(adminMapper.toDetailResponse(admin));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDetailResponse> getAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }
}
