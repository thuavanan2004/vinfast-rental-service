package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.AdminRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Create account admin")
    @PostMapping("/create")
    public ResponseData<?> create(@Valid @RequestBody AdminRequest request){
        log.info("Create account admin");
        try{
            adminService.create(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Create account admin successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create account admin failed");
        }
    }
}
