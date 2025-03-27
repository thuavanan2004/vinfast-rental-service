package com.vinfast.rental_service.modules.admin.application.service.Impl;

import com.vinfast.rental_service.modules.admin.application.dto.response.AdminDetailResponse;
import com.vinfast.rental_service.modules.admin.application.mapper.AdminMapper;
import com.vinfast.rental_service.modules.admin.application.service.AdminService;
import com.vinfast.rental_service.modules.admin.domain.entity.Admin;
import com.vinfast.rental_service.modules.admin.domain.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final AdminMapper adminMapper;

    @Override
    public AdminDetailResponse getAdminById(long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        return adminMapper.toDetailResponse(admin);
    }
}
