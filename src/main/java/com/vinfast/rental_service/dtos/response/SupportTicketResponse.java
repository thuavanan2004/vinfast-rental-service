package com.vinfast.rental_service.dtos.response;

import com.vinfast.rental_service.enums.TicketCategory;
import com.vinfast.rental_service.enums.TicketPriority;
import com.vinfast.rental_service.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SupportTicketResponse {
    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private TicketCategory category;
    private String resolution;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserInfo user;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String name;
        private String email;
    }

}
