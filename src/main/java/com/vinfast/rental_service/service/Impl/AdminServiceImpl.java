package com.vinfast.rental_service.service.Impl;


import com.vinfast.rental_service.dtos.request.*;
import com.vinfast.rental_service.dtos.response.*;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.PermissionMapper;
import com.vinfast.rental_service.mapper.RoleMapper;
import com.vinfast.rental_service.model.Admin;
import com.vinfast.rental_service.mapper.AdminMapper;
import com.vinfast.rental_service.model.Permission;
import com.vinfast.rental_service.model.Role;
import com.vinfast.rental_service.model.Token;
import com.vinfast.rental_service.repository.AdminRepository;
import com.vinfast.rental_service.repository.PermissionRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vinfast.rental_service.enums.TokenType.ACCESS_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AuthenticationManager authenticationManager;

    private final AdminRepository adminRepository;

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final TokenService tokenService;

    private final JwtService jwtService;

    private final AdminMapper adminMapper;

    private final RoleMapper roleMapper;

    private final PermissionMapper permissionMapper;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsService userDetailsService(){
        return username -> adminRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
    }

    @Override
    public AdminDetailResponse getAdminById(long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
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
    public void create(AdminCreateRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id:" + request.getRoleId()));

        Admin admin = adminMapper.toEntity(request);
        admin.setRole(role);
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        adminRepository.save(admin);
        log.info("Create account admin successfully");
    }

    @Override
    public void update(AdminUpdateRequest request, long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id:" + adminId));
        adminMapper.updateAdminFromDto(request, admin);

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id:" + request.getRoleId()));
        admin.setRole(role);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        adminRepository.save(admin);
        log.info("Update account admin successfully");
    }

    @Override
    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toDTO).toList();
    }

    @Override
    public void createRole(RoleRequest request) {
        if(roleRepository.existsByName(request.getName())){
            throw new InvalidDataException("Early access has been created");
        }
        Role role = roleMapper.toEntity(request);

        roleRepository.save(role);
    }

    @Transactional
    @Override
    public List<PermissionsResponse> getPermissionsByRoleId(long roleId) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        if (role.getPermissions() == null || role.getPermissions().isEmpty()) {
            throw new RuntimeException("No permissions found for role with id: " + roleId);
        }
        return role.getPermissions().stream().map(permissionMapper::toDTO).toList();
    }

    @Override
    public List<PermissionsResponse> getPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toDTO).toList();
    }

    @Override
    public void assignPermissionsToRole(long roleId, AssignPermissionsRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());

        Set<Permission> permissionSet = new HashSet<>(permissions);
        role.setPermissions(permissionSet);

        roleRepository.save(role);

        log.info("Permissions assigned successfully");
    }


}
