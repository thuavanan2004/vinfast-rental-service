package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.AdminLoginRequest;
import com.vinfast.rental_service.dtos.response.AdminDetailResponse;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin authentication")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthenticationController {
    private final AdminService adminService;

    @Operation
    @PostMapping("/login")
    public ResponseData<?> login(@Valid @RequestBody AdminLoginRequest request){
        log.info("Login for admin");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Login admin successfully", adminService.login(request));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Login admin failed");
        }
    }

    @Operation
    @PostMapping("/logout")
    public ResponseData<?> logout(HttpServletRequest request){
        log.info("Logout for admin");
        try{
            adminService.logout(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Logout admin successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Logout admin failed");
        }
    }

    @Operation(summary = "Get account admin")
    @GetMapping("/me/{id}")
    public ResponseData<?> getAdmin(@PathVariable Long id) {
        log.info("Get info for account admin");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get info for account admin successfully", adminService.getAdminById(id));
        } catch (Exception e) {
            log.info("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get info for account admin failed");
        }
    }
}
