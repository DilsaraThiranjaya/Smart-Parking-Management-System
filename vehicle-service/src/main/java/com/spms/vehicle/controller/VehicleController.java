package com.spms.vehicle.controller;

import com.spms.vehicle.dto.VehicleRequest;
import com.spms.vehicle.dto.VehicleEntryRequest;
import com.spms.vehicle.model.Vehicle;
import com.spms.vehicle.model.Vehicle.VehicleStatus;
import com.spms.vehicle.model.VehicleEntry;
import com.spms.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable("id") Long id) {
        try {
            Vehicle vehicle = vehicleService.getVehicleById(id);
            return ResponseEntity.ok(vehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/license/{licensePlate}")
    public ResponseEntity<Vehicle> getVehicleByLicensePlate(@PathVariable("licensePlate") String licensePlate) {
        try {
            Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
            return ResponseEntity.ok(vehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody VehicleRequest request) {
        try {
            Vehicle createdVehicle = vehicleService.createVehicle(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable("id") Long id,
                                               @Valid @RequestBody VehicleRequest request) {
        try {
            Vehicle updatedVehicle = vehicleService.updateVehicle(id, request);
            return ResponseEntity.ok(updatedVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("id") Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByOwner(@PathVariable("ownerId") String ownerId) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwner(ownerId);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Vehicle>> getVehiclesByStatus(@PathVariable("status") VehicleStatus status) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByStatus(status);
        return ResponseEntity.ok(vehicles);
    }
    
    @PostMapping("/entry")
    public ResponseEntity<VehicleEntry> simulateVehicleEntry(@Valid @RequestBody VehicleEntryRequest request) {
        try {
            VehicleEntry entry = vehicleService.simulateVehicleEntry(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(entry);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{vehicleId}/exit")
    public ResponseEntity<VehicleEntry> simulateVehicleExit(@PathVariable("vehicleId") Long vehicleId) {
        try {
            VehicleEntry entry = vehicleService.simulateVehicleExit(vehicleId);
            return ResponseEntity.ok(entry);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{vehicleId}/entries")
    public ResponseEntity<List<VehicleEntry>> getVehicleEntryHistory(@PathVariable("vehicleId") Long vehicleId) {
        List<VehicleEntry> entries = vehicleService.getVehicleEntryHistory(vehicleId);
        return ResponseEntity.ok(entries);
    }
    
    @GetMapping("/entries")
    public ResponseEntity<List<VehicleEntry>> getAllVehicleEntries() {
        List<VehicleEntry> entries = vehicleService.getAllVehicleEntries();
        return ResponseEntity.ok(entries);
    }
    
    @GetMapping("/entries/{entryId}")
    public ResponseEntity<VehicleEntry> getVehicleEntryById(@PathVariable("entryId") Long entryId) {
        try {
            VehicleEntry entry = vehicleService.getVehicleEntryById(entryId);
            return ResponseEntity.ok(entry);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/entries/active")
    public ResponseEntity<List<VehicleEntry>> getActiveVehicleEntries() {
        List<VehicleEntry> entries = vehicleService.getActiveVehicleEntries();
        return ResponseEntity.ok(entries);
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getVehicleStatistics() {
        Map<String, Object> stats = vehicleService.getVehicleStatistics();
        return ResponseEntity.ok(stats);
    }
}