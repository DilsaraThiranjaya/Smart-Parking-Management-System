package com.spms.vehicle.service;

import com.spms.vehicle.entity.Vehicle;
import com.spms.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }
    
    public Optional<Vehicle> getVehicleByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }
    
    public List<Vehicle> getVehiclesByUserId(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }
    
    public List<Vehicle> getVehiclesByType(Vehicle.VehicleType vehicleType) {
        return vehicleRepository.findByVehicleType(vehicleType);
    }
    
    public List<Vehicle> getVehiclesByMake(String make) {
        return vehicleRepository.findByMake(make);
    }
    
    public List<Vehicle> getVehiclesByMakeAndModel(String make, String model) {
        return vehicleRepository.findByMakeAndModel(make, model);
    }
    
    public List<Vehicle> getParkedVehicles() {
        return vehicleRepository.findByCurrentParkingSpaceIdIsNotNull();
    }
    
    public List<Vehicle> getParkedVehiclesByUserId(Long userId) {
        return vehicleRepository.findParkedVehiclesByUserId(userId);
    }
    
    public Vehicle createVehicle(Vehicle vehicle) {
        // Check if license plate already exists
        if (vehicleRepository.findByLicensePlate(vehicle.getLicensePlate()).isPresent()) {
            throw new RuntimeException("Vehicle with license plate " + vehicle.getLicensePlate() + " already exists");
        }
        return vehicleRepository.save(vehicle);
    }
    
    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        
        // Check if new license plate already exists (if changed)
        if (!vehicle.getLicensePlate().equals(vehicleDetails.getLicensePlate())) {
            if (vehicleRepository.findByLicensePlate(vehicleDetails.getLicensePlate()).isPresent()) {
                throw new RuntimeException("Vehicle with license plate " + vehicleDetails.getLicensePlate() + " already exists");
            }
        }
        
        vehicle.setLicensePlate(vehicleDetails.getLicensePlate());
        vehicle.setMake(vehicleDetails.getMake());
        vehicle.setModel(vehicleDetails.getModel());
        vehicle.setYear(vehicleDetails.getYear());
        vehicle.setColor(vehicleDetails.getColor());
        vehicle.setVehicleType(vehicleDetails.getVehicleType());
        
        return vehicleRepository.save(vehicle);
    }
    
    public Vehicle simulateVehicleEntry(Long vehicleId, Long parkingSpaceId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
        
        if (vehicle.getCurrentParkingSpaceId() != null) {
            throw new RuntimeException("Vehicle is already parked");
        }
        
        vehicle.setCurrentParkingSpaceId(parkingSpaceId);
        vehicle.setEntryTime(LocalDateTime.now());
        vehicle.setExitTime(null);
        
        return vehicleRepository.save(vehicle);
    }
    
    public Vehicle simulateVehicleExit(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
        
        if (vehicle.getCurrentParkingSpaceId() == null) {
            throw new RuntimeException("Vehicle is not currently parked");
        }
        
        vehicle.setCurrentParkingSpaceId(null);
        vehicle.setExitTime(LocalDateTime.now());
        
        return vehicleRepository.save(vehicle);
    }
    
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        
        if (vehicle.getCurrentParkingSpaceId() != null) {
            throw new RuntimeException("Cannot delete vehicle that is currently parked. Please exit the vehicle first.");
        }
        
        vehicleRepository.delete(vehicle);
    }
    
    public Long getParkedVehiclesCount() {
        return vehicleRepository.countParkedVehicles();
    }
}