package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.response.SupportTicketResponse;
import com.vinfast.rental_service.model.Admin;
import com.vinfast.rental_service.model.SupportTicket;
import com.vinfast.rental_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SupportTicketMapper {

    @Mapping(source = "user", target = "user", qualifiedByName = "mapByUser")
    SupportTicketResponse toDTO(SupportTicket entity);

    @Named("mapByUser")
    default SupportTicketResponse.UserInfo mapByUser(User user){
        return new SupportTicketResponse.UserInfo(user.getId(), user.getName(), user.getEmail());
    }
}
