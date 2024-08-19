package com.express.user.service;


import com.express.user.dto.VehicleRequest;
import com.express.user.dto.VehicleResponse;
import com.express.user.dtoMaper.VehicleDtoMapper;
import com.express.user.entity.Vehicle;
import com.express.user.exception.ResourceNotFoundException;
import com.express.user.repo.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse createVehicle(VehicleRequest vehicleRequest) {
        log.info("VehicleService::createVehicle()::Start");
        if (vehicleRepository.existsByVehicleNumber(vehicleRequest.getVehicleNumber())) {
            log.error("VehicleService::createVehicle()::Vehicle already exists with number: {}",
                    vehicleRequest.getVehicleNumber());
            throw new ResourceNotFoundException("Vehicle already exists with number: " + vehicleRequest.getVehicleNumber());
        }
        Vehicle vehicle = VehicleDtoMapper.toVehicleRequest(vehicleRequest);
        vehicle = vehicleRepository.save(vehicle);
        log.info("VehicleService::createVehicle()::Vehicle created successfully with number: {}", vehicle.getVehicleNumber());
        return VehicleDtoMapper.toVehicleResponse(vehicle);
    }

    public VehicleResponse getVehicleById(Long vehicleId) {
        log.info("VehicleService::getVehicleById()::Start");
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> {
                    log.error("VehicleService::getVehicleById()::Vehicle not found with id: {}", vehicleId);
                    return new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
                });
        log.info("VehicleService::getVehicleById()::Vehicle found with id: {}", vehicleId);
        return VehicleDtoMapper.toVehicleResponse(vehicle);
    }


    public VehicleResponse updateVehicle(Long vehicleId, VehicleRequest vehicleRequest) {
        log.info("VehicleService::updateVehicle()::Start");
        if (!vehicleRepository.existsById(vehicleId)) {
            log.error("VehicleService::updateVehicle()::Vehicle not found with id: {}", vehicleId);
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        }
        if (vehicleRepository.existsByVehicleNumberAndIdNot(vehicleRequest.getVehicleNumber(), vehicleId)) {
            log.error("VehicleService::updateVehicle()::Vehicle already exists with number: {}",
                    vehicleRequest.getVehicleNumber());
            throw new ResourceNotFoundException("Vehicle already exists with number: " + vehicleRequest.getVehicleNumber());
        }
        Vehicle vehicle = VehicleDtoMapper.toVehicleRequest(vehicleRequest);
        vehicle.setId(vehicleId);
        log.info("VehicleService::updateVehicle()::Vehicle updated successfully with id: {}", vehicleId);
        return VehicleDtoMapper.toVehicleResponse(vehicle);
    }


    public List<VehicleResponse> getAllVehicles() {
        log.info("VehicleService::getAllVehicles()::Start");
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            log.error("VehicleService::getAllVehicles()::No vehicles found");
            throw new ResourceNotFoundException("No vehicles found");
        }
        log.info("VehicleService::getAllVehicles()::Vehicles found: {}", vehicles.size());
        return vehicles.stream().map(VehicleDtoMapper::toVehicleResponse).toList();
    }
}
