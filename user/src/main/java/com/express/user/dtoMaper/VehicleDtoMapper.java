package com.express.user.dtoMaper;

import com.express.user.dto.VehicleRequest;
import com.express.user.dto.VehicleResponse;
import com.express.user.entity.Vehicle;

public class VehicleDtoMapper {

    public static Vehicle toVehicleRequest(VehicleRequest vehicleRequest) {
        return Vehicle.builder()
                .vehicleNumber(vehicleRequest.getVehicleNumber())
                .vehicleType(vehicleRequest.getVehicleType())
                .vehicleCapacity(vehicleRequest.getVehicleCapacity())
                .vehicleStatus(vehicleRequest.getVehicleStatus())
                .build();
    }

    public static VehicleResponse toVehicleResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .vehicleType(vehicle.getVehicleType())
                .vehicleCapacity(vehicle.getVehicleCapacity())
                .vehicleStatus(vehicle.getVehicleStatus())
                .build();
    }

}
