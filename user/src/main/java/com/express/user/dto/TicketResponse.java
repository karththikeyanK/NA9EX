package com.express.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {
    private Long id;
    private String ticketNumber;
    private int maleCount;
    private int femaleCount;
    private String route;
    private Long pickupPointId;
    private String pickupPoint;
    private Long dropPointId;
    private String dropPoint;
    private LocalDate date;
    private String status;
    private String description;
    private Long customerId;
    private LocalDate createdAt;
    private Long issuerId;
    private String msg;
}
