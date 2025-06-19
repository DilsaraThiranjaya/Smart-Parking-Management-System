package com.spms.vehicle.controller;

import com.spms.vehicle.entity.Vehicle;
import com.spms.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        return vehicle.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/license-plate/{licensePlate}")
    public ResponseEntity<Vehicle> getVehicleByLicensePlate(@PathVariable String licensePlate) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
        return vehicle.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByUserId(@PathVariable Long userId) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/type/{vehicleType}")
    public ResponseEntity<List<Vehicle>> getVehiclesByType(@PathVariable Vehicle.VehicleType vehicleType) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByType(vehicleType);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/make/{make}")
    public ResponseEntity<List<Vehicle>> getVehiclesByMake(@PathVariable String make) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByMake(make);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/make/{make}/model/{model}")
    public ResponseEntity<List<Vehicle>> getVehiclesByMakeAndModel(
            @PathVariable String make, @PathVariable String model) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByMakeAndModel(make, model);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/parked")
    public ResponseEntity<List<Vehicle>> getParkedVehicles() {
        List<Vehicle> vehicles = vehicleService.getParkedVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/parked/user/{userId}")
    public ResponseEntity<List<Vehicle>> getParkedVehiclesByUserId(@PathVariable Long userId) {
        List<Vehicle> vehicles = vehicleService.getParkedVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }
    
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        try {
            Vehicle createdVehicle = vehicleService.createVehicle(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id, @Valid @RequestBody Vehicle vehicleDetails) {
        try {
            Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicleDetails);
            return ResponseEntity.ok(updatedVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/entry")
    public ResponseEntity<Vehicle> simulateVehicleEntry(
            @PathVariable Long id, @RequestBody Map<String, Long> request) {
        try {
            Long parkingSpaceId = request.get("parkingSpaceId");
            if (parkingSpaceId == null) {
                return ResponseEntity.badRequest().build();
            }
            Vehicle vehicle = vehicleService.simulateVehicleEntry(id, parkingSpaceId);
            return ResponseEntity.ok(vehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/exit")
    public ResponseEntity<Vehicle> simulateVehicleExit(@PathVariable Long id) {
        try {
            Vehicle vehicle = vehicleService.simulateVehicleExit(id);
            return ResponseEntity.ok(vehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/stats/parked-count")
    public ResponseEntity<Map<String, Long>> getParkedVehiclesCount() {
        Long count = vehicleService.getParkedVehiclesCount();
        return ResponseEntity.ok(Map.of("parkedVehicles", count));
    }
}