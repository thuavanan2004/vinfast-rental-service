package com.vinfast.rental_service.service.Impl;


import com.vinfast.rental_service.dtos.request.AdminRequest;
import com.vinfast.rental_service.dtos.request.AdminLoginRequest;
import com.vinfast.rental_service.dtos.response.AdminAuthResponse;
import com.vinfast.rental_service.dtos.response.AdminDetailResponse;
import com.vinfast.rental_service.dtos.response.AdminProfileResponse;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.model.Admin;
import com.vinfast.rental_service.mapper.AdminMapper;
import com.vinfast.rental_service.model.Role;
import com.vinfast.rental_service.model.Token;
import com.vinfast.rental_service.repository.AdminRepository;
import com.vinfast.rental_service.repository.RoleRepository;
import com.vinfast.rental_service.service.AdminService;
import com.vinfast.rental_service.service.JwtService;
import com.vinfast.rental_service.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.vinfast.rental_service.enums.TokenType.ACCESS_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AuthenticationManager authenticationManager;

    private final AdminRepository adminRepository;

    private final RoleRepository roleRepository;

    private final TokenService tokenService;

    private final JwtService jwtService;

    private final AdminMapper adminMapper;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsService userDetailsService(){
        return username -> adminRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
    }

    @Override
    public AdminDetailResponse getAdminById(long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        return adminMapper.toDetailResponse(admin);
    }

    @Override
    public AdminAuthResponse login(AdminLoginRequest request) {
        log.info("---------- login ----------");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var admin = adminRepository.findByUsername(request.getUsername()).orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        var accessToken = jwtService.generateAccessToken(admin);

        tokenService.save(Token.builder().accessToken(accessToken).username(admin.getUsername()).build());
        AdminProfileResponse response = adminMapper.toProfileResponse(admin);

        return AdminAuthResponse.builder()
                .accessToken(accessToken)
                .profile(response)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        log.info("---------- logout ----------");

        final String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            throw new InvalidDataException("Token must be not blank");
        }

        final String email = jwtService.extractUserName(token, ACCESS_TOKEN);
        tokenService.delete(email);

        log.info("Logout successfully!");
    }

    @Override
    public void create(AdminRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id:" + request.getRoleId()));

        Admin admin = adminMapper.toEntity(request);
        admin.setRole(role);
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        adminRepository.save(admin);
        log.info("Create account admin successfully");
    }


}
