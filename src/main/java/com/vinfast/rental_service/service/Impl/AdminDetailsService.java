package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with username:{} " + username));
    }

    public boolean existByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }
}
