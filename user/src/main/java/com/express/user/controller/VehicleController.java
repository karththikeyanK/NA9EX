package com.express.user.controller;


import com.express.user.dto.VehicleRequest;
import com.express.user.dto.VehicleResponse;
import com.express.user.response.ApiResponse;
import com.express.user.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VehicleResponse>> createVehicle(@RequestBody VehicleRequest vehicleRequest){
        return ResponseEntity.ok(new ApiResponse<VehicleResponse>(ApiResponse.SUCCESS, "Vehicle created successfully", vehicleService.createVehicle(vehicleRequest)));
    }

    @GetMapping("/get/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicle(@PathVariable Long vehicleId){
        return ResponseEntity.ok(new ApiResponse<VehicleResponse>(ApiResponse.SUCCESS, "Vehicle fetched successfully", vehicleService.getVehicleById(vehicleId)));
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(@RequestBody VehicleRequest vehicleRequest,@PathVariable Long vehicleId){
        return ResponseEntity.ok(new ApiResponse<VehicleResponse>(ApiResponse.SUCCESS, "Vehicle updated successfully", vehicleService.updateVehicle(vehicleId, vehicleRequest)));
    }


    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getAllVehicles(){
        return ResponseEntity.ok(new ApiResponse<List<VehicleResponse>>(ApiResponse.SUCCESS, "Vehicles fetched successfully", vehicleService.getAllVehicles()));
    }
}
