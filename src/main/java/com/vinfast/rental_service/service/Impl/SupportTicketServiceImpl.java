package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.SupportTicketResponse;
import com.vinfast.rental_service.enums.TicketStatus;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.SupportTicketMapper;
import com.vinfast.rental_service.model.Admin;
import com.vinfast.rental_service.model.SupportTicket;
import com.vinfast.rental_service.model.User;
import com.vinfast.rental_service.repository.AdminRepository;
import com.vinfast.rental_service.repository.SupportTicketRepository;
import com.vinfast.rental_service.repository.UserRepository;
import com.vinfast.rental_service.service.EmailService;
import com.vinfast.rental_service.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;

    private final EmailService emailService;

    private final SupportTicketMapper supportTicketMapper;

    @Override
    public PageResponse<?> getAll(Pageable pageable) {
        Page<SupportTicket> supportTickets = supportTicketRepository.findAll(pageable);
        List<SupportTicketResponse> results = supportTickets.stream()
                .map(supportTicketMapper::toDTO)
                .collect(Collectors.toList());

        return PageResponse.builder()
                .page(supportTickets.getNumber())
                .size(supportTickets.getSize())
                .totalPage(supportTickets.getTotalPages())
                .items(results)
                .build();
    }

    @Override
    public void assign(long supportTicketId, long adminId) {
        SupportTicket supportTicket = supportTicketRepository.findById(supportTicketId)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found"));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        User user = userRepository.findById(supportTicket.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User request support ticket not found with id: " + supportTicket.getUser().getId()));

        if(supportTicket.getAssignedAdmin() == admin){
            throw new InvalidDataException("The administrator has been assigned");
        }
        supportTicket.setAssignedAdmin(admin);

        supportTicketRepository.save(supportTicket);

        emailService.sendAdminSupportInfo(user.getEmail(), admin.getFullName(), admin.getEmail(), admin.getPhone());

        log.info("Successful processing assignment");
    }

    @Override
    public void resolve(long supportTicketId, TicketStatus status) {
        SupportTicket supportTicket = supportTicketRepository.findById(supportTicketId)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found"));

        if(supportTicket.getStatus() == status){
            throw new InvalidDataException("Early state has been used");
        }
        supportTicket.setStatus(status);

        supportTicketRepository.save(supportTicket);

        log.info("Change status support ticket successfully");
    }

    @Override
    public SupportTicketResponse getSupportTicketById(long supportTicketId) {
        SupportTicket supportTicket = supportTicketRepository.findById(supportTicketId)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticker not found with id: " + supportTicketId));
        return supportTicketMapper.toDTO(supportTicket);
    }

    @Override
    public void reply(long supportTicketId) {
        SupportTicket supportTicket = supportTicketRepository.findById(supportTicketId)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticker not found with id: " + supportTicketId));
        User user = userRepository.findById(supportTicket.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User request support ticket not found with id: " + supportTicket.getUser().getId()));

        emailService.replySupport(user.getEmail());
    }

}
