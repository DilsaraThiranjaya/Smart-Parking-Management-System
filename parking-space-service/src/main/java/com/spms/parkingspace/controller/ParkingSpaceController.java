package com.spms.parkingspace.controller;

import com.spms.parkingspace.dto.ParkingSpaceRequest;
import com.spms.parkingspace.dto.ReservationRequest;
import com.spms.parkingspace.model.ParkingSpace;
import com.spms.parkingspace.model.ParkingSpace.SpaceStatus;
import com.spms.parkingspace.service.ParkingSpaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/parking-spaces")
@CrossOrigin(origins = "*")
public class ParkingSpaceController {
    
    @Autowired
    private ParkingSpaceService parkingSpaceService;
    
    @GetMapping
    public ResponseEntity<List<ParkingSpace>> getAllParkingSpaces() {
        List<ParkingSpace> spaces = parkingSpaceService.getAllParkingSpaces();
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpace> getParkingSpaceById(@PathVariable("id") Long id) {
        try {
            ParkingSpace space = parkingSpaceService.getParkingSpaceById(id);
            return ResponseEntity.ok(space);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ParkingSpace> createParkingSpace(@Valid @RequestBody ParkingSpaceRequest request) {
        try {
            ParkingSpace createdSpace = parkingSpaceService.createParkingSpace(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpace> updateParkingSpace(@PathVariable("id") Long id,
                                                          @Valid @RequestBody ParkingSpaceRequest request) {
        try {
            ParkingSpace updatedSpace = parkingSpaceService.updateParkingSpace(id, request);
            return ResponseEntity.ok(updatedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpace(@PathVariable("id") Long id) {
        try {
            parkingSpaceService.deleteParkingSpace(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpace>> getAvailableParkingSpaces() {
        List<ParkingSpace> availableSpaces = parkingSpaceService.getAvailableParkingSpaces();
        return ResponseEntity.ok(availableSpaces);
    }
    
    @GetMapping("/city/{city}")
    public ResponseEntity<List<ParkingSpace>> getParkingSpacesByCity(@PathVariable("city") String city) {
        List<ParkingSpace> spaces = parkingSpaceService.getParkingSpacesByCity(city);
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/zone/{zone}")
    public ResponseEntity<List<ParkingSpace>> getParkingSpacesByZone(@PathVariable("zone") String zone) {
        List<ParkingSpace> spaces = parkingSpaceService.getParkingSpacesByZone(zone);
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ParkingSpace>> getParkingSpacesByOwner(@PathVariable("ownerId") String ownerId) {
        List<ParkingSpace> spaces = parkingSpaceService.getParkingSpacesByOwner(ownerId);
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/available/city/{city}")
    public ResponseEntity<List<ParkingSpace>> getAvailableParkingSpacesByCity(@PathVariable("city") String city) {
        List<ParkingSpace> spaces = parkingSpaceService.getAvailableParkingSpacesByCity(city);
        return ResponseEntity.ok(spaces);
    }
    
    @GetMapping("/available/zone/{zone}")
    public ResponseEntity<List<ParkingSpace>> getAvailableParkingSpacesByZone(@PathVariable("zone") String zone) {
        List<ParkingSpace> spaces = parkingSpaceService.getAvailableParkingSpacesByZone(zone);
        return ResponseEntity.ok(spaces);
    }
    
    @PostMapping("/reserve")
    public ResponseEntity<ParkingSpace> reserveParkingSpace(@Valid @RequestBody ReservationRequest request) {
        try {
            ParkingSpace reservedSpace = parkingSpaceService.reserveParkingSpace(request);
            return ResponseEntity.ok(reservedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/release")
    public ResponseEntity<ParkingSpace> releaseParkingSpace(@PathVariable("id") Long id) {
        try {
            ParkingSpace releasedSpace = parkingSpaceService.releaseParkingSpace(id);
            return ResponseEntity.ok(releasedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/occupy")
    public ResponseEntity<ParkingSpace> occupyParkingSpace(@PathVariable("id") Long id) {
        try {
            ParkingSpace occupiedSpace = parkingSpaceService.occupyParkingSpace(id);
            return ResponseEntity.ok(occupiedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<ParkingSpace> updateSpaceStatus(@PathVariable("id") Long id,
                                                         @RequestParam SpaceStatus status) {
        try {
            ParkingSpace updatedSpace = parkingSpaceService.updateSpaceStatus(id, status);
            return ResponseEntity.ok(updatedSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getParkingStatistics() {
        Map<String, Object> stats = parkingSpaceService.getParkingStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/statistics/city/{city}")
    public ResponseEntity<Map<String, Object>> getParkingStatisticsByCity(@PathVariable("city") String city) {
        Map<String, Object> stats = parkingSpaceService.getParkingStatisticsByCity(city);
        return ResponseEntity.ok(stats);
    }
}