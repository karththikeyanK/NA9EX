package com.express.user.dtoMaper;

import com.express.user.dto.TicketAndCustomerResponse;
import com.express.user.dto.TicketRequest;
import com.express.user.dto.TicketResponse;
import com.express.user.entity.Address;
import com.express.user.entity.Customer;
import com.express.user.entity.Ticket;
import com.express.user.entity.User;

public class TicketDtoMapper {

    public static Ticket mapToTicket(TicketRequest ticketRequest, User issuer, Customer customer, Address pickupPoint, Address dropPoint) {
        return Ticket.builder()
                .ticketNumber(ticketRequest.getTicketNumber())
                .maleCount(ticketRequest.getMaleCount())
                .femaleCount(ticketRequest.getFemaleCount())
                .toWhere(ticketRequest.getToWhere())
                .pickupPoint(pickupPoint)
                .dropPoint(dropPoint)
                .date(ticketRequest.getDate())
                .status(ticketRequest.getStatus())
                .customer(customer)
                .issuer(issuer)
                .msg(ticketRequest.getMsg())
                .description(ticketRequest.getDescription())
                .build();
    }

    public static TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .maleCount(ticket.getMaleCount())
                .femaleCount(ticket.getFemaleCount())
                .route(ticket.getToWhere())
                .pickupPointId(ticket.getPickupPoint() != null ? ticket.getPickupPoint().getId() : null)
                .pickupPoint(ticket.getPickupPoint() != null ? ticket.getPickupPoint().getAddress() : null)
                .dropPointId(ticket.getDropPoint() != null ? ticket.getDropPoint().getId() : null)
                .dropPoint(ticket.getDropPoint() != null ? ticket.getDropPoint().getAddress() : null)
                .date(ticket.getDate())
                .status(ticket.getStatus())
                .customerId(ticket.getCustomer().getId())
                .issuerId(ticket.getIssuer().getId())
                .msg(ticket.getMsg())
                .description(ticket.getDescription())
                .createdAt(ticket.getCreatedAt())
                .build();
    }


    public static TicketAndCustomerResponse mapToTicketAndCustomerResponse(Ticket ticket) {
        return TicketAndCustomerResponse.builder()
                .ticketResponse(mapToTicketResponse(ticket))
                .customerResponse(CustomerDtoMapper.mapToCustomerResponse(ticket.getCustomer()))
                .build();
    }
}
