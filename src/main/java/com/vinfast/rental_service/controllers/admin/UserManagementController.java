package com.vinfast.rental_service.controllers.admin;

import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin authentication")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserService userService;

    @Operation(summary = "Get list users for admin management")
    @GetMapping
    public ResponseData<?> getAll(Pageable pageable, @RequestParam(required = false) String[] users){
        log.info("Get list users for admin management");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get list users successfully", userService.getAll(pageable, users));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list users failed");
        }
    }
}
