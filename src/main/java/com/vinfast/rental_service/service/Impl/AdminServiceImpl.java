package com.vinfast.rental_service.service.Impl;


import com.vinfast.rental_service.dtos.response.AdminDetailResponse;
import com.vinfast.rental_service.entity.Admin;
import com.vinfast.rental_service.mapper.AdminMapper;
import com.vinfast.rental_service.repository.AdminRepository;
import com.vinfast.rental_service.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
//@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    @Override
    public AdminDetailResponse getAdminById(long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        return adminMapper.toDetailResponse(admin);
    }
}
