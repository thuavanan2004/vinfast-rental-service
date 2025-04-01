package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.SupportTicketResponse;
import com.vinfast.rental_service.enums.TicketStatus;
import org.springframework.data.domain.Pageable;


public interface SupportTicketService {
    PageResponse<?> getAll(Pageable pageable);

    void assign(long supportTicketId, long adminId);

    void resolve(long supportTicketId, TicketStatus status);

    SupportTicketResponse getSupportTicketById(long supportTicketId);
}
