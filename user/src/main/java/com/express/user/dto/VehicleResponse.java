package com.express.user.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VehicleResponse {
    private Long id;
    private String vehicleNumber;
    private String vehicleType;
    private int vehicleCapacity;
    private String vehicleStatus;
}
