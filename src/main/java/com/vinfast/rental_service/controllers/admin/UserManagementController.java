package com.vinfast.rental_service.controllers.admin;

import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.enums.UserStatus;
import com.vinfast.rental_service.service.UserService;
import com.vinfast.rental_service.validate.EnumPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "User management")
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

    @Operation(summary = "Get info detail user")
    @GetMapping("/{userId}")
    public ResponseData<?> getUser(@PathVariable long userId){
        log.info("Get info detail user");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get info detail user successfully", userService.getUser(userId));
        } catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get info detail user failed");
        }
    }

    @Operation(summary = "Change status for user (active|inactive|suspended)")
    @PutMapping("/status/{userId}")
    public ResponseData<?> changeStatus(@PathVariable long userId, @RequestParam @EnumPattern(name="status", regexp = "active|banned|suspended") UserStatus status){
        log.info("Change status for user");
        try{
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Change status successfully");
        } catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change status failed");
        }
    }

    @Operation(summary = "Get user's rental history")
    @GetMapping("/bookings/{userId}")
    public ResponseData<?> rentalHistory(@PathVariable long userId,
                                         Pageable pageable){
        log.info("Fetching rental history for user: {}", userId);
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get user's rental history successfully", userService.rentalHistory(userId, pageable));
        } catch (EntityNotFoundException e) {
            log.warn("User not found: {}", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            log.error("Error fetching rental history for user {}: {}", userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve rental history");
        }
    }
}
