package com.vinfast.rental_service.controllers.admin;

import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.enums.TicketStatus;
import com.vinfast.rental_service.service.SupportTicketService;
import com.vinfast.rental_service.validate.EnumPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Support ticket management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/support-tickets")
@RequiredArgsConstructor
public class AdminSupportTicketController {
    private final SupportTicketService supportTicketService;


    @Operation(summary = "Get list support ticket")
    @GetMapping
    public ResponseData<?> getAll(Pageable pageable){
        log.info("Get list support ticket");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get list support ticket successfully", supportTicketService.getAll(pageable));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list support ticket failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Processing assignment")
    @PutMapping("/{supportTicketId}/assign/{adminId}")
    public ResponseData<?> assign(@PathVariable @Min(1) long supportTicketId, @PathVariable @Min(1) long adminId){
        log.info("Processing assignment");
        try{
            supportTicketService.assign(supportTicketId, adminId);
            return new ResponseData<>(HttpStatus.OK.value(), "Processing assignment successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Processing assignment failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Change status support ticket")
    @PatchMapping("/{supportTicketId}/resolve")
    public ResponseData<?> resolve(@PathVariable @Min(1) long supportTicketId,
                                    @RequestParam @EnumPattern(name = "status", regexp = "open|in_progress|resolved|closed")TicketStatus status){
        log.info("Change status support ticket");
        try{
            supportTicketService.resolve(supportTicketId, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Change status support ticket successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change status support ticket failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Reply support ticket")
    @PostMapping("/{supportTicketId}/reply")
    public ResponseData<?> reply(@PathVariable @Min(1) long supportTicketId){
        log.info("Reply support ticket");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Reply support ticket successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Reply support ticket failed: " + e.getMessage());
        }
    }
}
