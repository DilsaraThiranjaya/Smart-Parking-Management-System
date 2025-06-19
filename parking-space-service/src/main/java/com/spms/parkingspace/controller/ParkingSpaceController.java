package com.spms.parkingspace.controller;

import com.spms.parkingspace.entity.ParkingSpace;
import com.spms.parkingspace.service.ParkingSpaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking-spaces")
public class ParkingSpaceController {
    
    @Autowired
    private ParkingSpaceService parkingSpaceService;
    
    @GetMapping
    public ResponseEntity<List<ParkingSpace>> getAllParkingSpaces() {
        List<ParkingSpace> spaces = parkingSpaceService.getAllParkingSpaces();
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpace> getParkingSpaceById(@PathVariable Long id) {
        Optional<ParkingSpace> space = parkingSpaceService.getParkingSpaceById(id);
        return space.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/space-number/{spaceNumber}")
    public ResponseEntity<ParkingSpace> getParkingSpaceByNumber(@PathVariable String spaceNumber) {
        Optional<ParkingSpace> space = parkingSpaceService.getParkingSpaceByNumber(spaceNumber);
        return space.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpace>> getAvailableParkingSpaces() {
        List<ParkingSpace> spaces = parkingSpaceService.getAvailableParkingSpaces();
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/city/{city}")
    public ResponseEntity<List<ParkingSpace>> getParkingSpacesByCity(@PathVariable String city) {
        List<ParkingSpace> spaces = parkingSpaceService.getParkingSpacesByCity(city);
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/zone/{zone}")
    public ResponseEntity<List<ParkingSpace>> getParkingSpacesByZone(@PathVariable String zone) {
        List<ParkingSpace> spaces = parkingSpaceService.getParkingSpacesByZone(zone);
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ParkingSpace>> getParkingSpacesByOwner(@PathVariable Long ownerId) {
        List<ParkingSpace> spaces = parkingSpaceService.getParkingSpacesByOwner(ownerId);
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/available/city/{city}/zone/{zone}")
    public ResponseEntity<List<ParkingSpace>> getAvailableSpacesByCityAndZone(
            @PathVariable String city, @PathVariable String zone) {
        List<ParkingSpace> spaces = parkingSpaceService.getAvailableSpacesByCityAndZone(city, zone);
        return ResponseEntity.ok(spaces);
    }
    
    @PostMapping
    public ResponseEntity<ParkingSpace> createParkingSpace(@Valid @RequestBody ParkingSpace parkingSpace) {
        try {
            ParkingSpace createdSpace = parkingSpaceService.createParkingSpace(parkingSpace);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpace> updateParkingSpace(
            @PathVariable Long id, @Valid @RequestBody ParkingSpace parkingSpaceDetails) {
        try {
            ParkingSpace updatedSpace = parkingSpaceService.updateParkingSpace(id, parkingSpaceDetails);
            return ResponseEntity.ok(updatedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/reserve")
    public ResponseEntity<ParkingSpace> reserveParkingSpace(
            @PathVariable Long id, @RequestBody Map<String, Integer> request) {
        try {
            int durationHours = request.getOrDefault("durationHours", 1);
            ParkingSpace reservedSpace = parkingSpaceService.reserveParkingSpace(id, durationHours);
            return ResponseEntity.ok(reservedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/release")
    public ResponseEntity<ParkingSpace> releaseParkingSpace(@PathVariable Long id) {
        try {
            ParkingSpace releasedSpace = parkingSpaceService.releaseParkingSpace(id);
            return ResponseEntity.ok(releasedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/occupy")
    public ResponseEntity<ParkingSpace> occupyParkingSpace(@PathVariable Long id) {
        try {
            ParkingSpace occupiedSpace = parkingSpaceService.occupyParkingSpace(id);
            return ResponseEntity.ok(occupiedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpace(@PathVariable Long id) {
        try {
            parkingSpaceService.deleteParkingSpace(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/stats/available-count/city/{city}")
    public ResponseEntity<Map<String, Long>> getAvailableSpacesCount(@PathVariable String city) {
        Long count = parkingSpaceService.getAvailableSpacesCount(city);
        return ResponseEntity.ok(Map.of("availableSpaces", count));
    }
}