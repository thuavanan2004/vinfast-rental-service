package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.AdminCreateRequest;
import com.vinfast.rental_service.dtos.request.AdminUpdateRequest;
import com.vinfast.rental_service.dtos.request.AssignPermissionsRequest;
import com.vinfast.rental_service.dtos.request.RoleRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin & Role management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PreAuthorize("hasAuthority('admin:read')")
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

    @PreAuthorize("hasAuthority('admin:create')")
    @Operation(summary = "Create account admin")
    @PostMapping("/create")
    public ResponseData<?> create(@Valid @RequestBody AdminCreateRequest request){
        log.info("Create account admin");
        try{
            adminService.create(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Create account admin successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create account admin failed");
        }
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @Operation(summary = "Update account admin")
    @PutMapping("/update/{adminId}")
    public ResponseData<?> update(@Valid @RequestBody AdminUpdateRequest request, @PathVariable @Min(1) long adminId){
        log.info("Update account admin");
        try{
            adminService.update(request, adminId);
            return new ResponseData<>(HttpStatus.OK.value(), "Update account admin successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update account adminn failed");
        }
    }

    @PreAuthorize("hasAuthority('role:read')")
    @Operation(summary = "Get all role")
    @GetMapping("/roles")
    public ResponseData<?> getRoles(){
        log.info("Get all role");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get all role successfully", adminService.getRoles());
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all role failed");
        }
    }

    @PreAuthorize("hasAuthority('role:read')")
    @Operation(summary = "Create role")
    @PostMapping("/roles/create")
    public ResponseData<?> createRole(@Valid @RequestBody RoleRequest request){
        log.info("Create role");
        try{
            adminService.createRole(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Create role successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create role failed");
        }
    }

    @PreAuthorize("hasAuthority('role:read')")
    @Operation(summary = "Get all permissions")
    @GetMapping("/permissions")
    public ResponseData<?> getPermissions(){
        log.info("Get all permissions");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get all permissions successfully", adminService.getPermissions());
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get all permissions failed");
        }
    }

    @PreAuthorize("hasAuthority('role:read')")
    @Operation(summary = "Get list of permissions by role")
    @GetMapping("/permissions/{roleId}")
    public ResponseData<?> getPermissionsByRoleId(@PathVariable @Min(1) long roleId){
        log.info("Get list of permissions by role");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get list of permissions by role successfully", adminService.getPermissionsByRoleId(roleId));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list of permissions by role failed");
        }
    }

    @PreAuthorize("hasAuthority('permission:assign')")
    @Operation(summary = "Permissions assigned")
    @PostMapping("/permissions/{roleId}")
    public ResponseData<?> assignPermissionsToRole(@PathVariable @Min(1) long roleId,
                                                   @RequestBody @Valid AssignPermissionsRequest request){
        log.info("Permissions assigned");
        try{
            adminService.assignPermissionsToRole(roleId, request);
            return new ResponseData<>(HttpStatus.OK.value(),"Permissions assigned successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Permissions assigned failed");
        }
    }
}
