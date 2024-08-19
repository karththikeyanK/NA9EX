package com.express.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    private String ticketNumber;
    private int maleCount;
    private int femaleCount;
    private String mobileNumber;
    private String customerName;
    private String toWhere;
    private Long pickupPointId;
    private String pickupPoint;
    private Long dropPointId;
    private String dropPoint;
    private LocalDate date;
    private String status;
    private Long userId;
    private Long customerId;
    private String description;
    private String msg;
}
