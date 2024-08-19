package com.express.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetails {
    private String Route;
    private LocalDate date;
    private int maleCount;
    private int femaleCount;
    private int pending;
    private int pendingCount;
    private int total;

}
