package com.express.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VehicleRequest {
    private String vehicleNumber;
    private String vehicleType;
    private int vehicleCapacity;
    private String vehicleStatus;
}
