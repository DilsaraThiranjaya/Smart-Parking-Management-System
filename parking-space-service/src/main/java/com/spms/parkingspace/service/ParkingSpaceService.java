package com.spms.parkingspace.service;

import com.spms.parkingspace.dto.ParkingSpaceRequest;
import com.spms.parkingspace.dto.ReservationRequest;
import com.spms.parkingspace.model.ParkingSpace;
import com.spms.parkingspace.model.ParkingSpace.SpaceStatus;
import com.spms.parkingspace.repository.ParkingSpaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class ParkingSpaceService {
    
    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;
    
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceRepository.findAll();
    }
    
    public ParkingSpace getParkingSpaceById(Long id) {
        return parkingSpaceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parking space not found with id: " + id));
    }
    
    public ParkingSpace createParkingSpace(ParkingSpaceRequest request) {
        // Check if space number already exists
        if (parkingSpaceRepository.findBySpaceNumber(request.getSpaceNumber()).isPresent()) {
            throw new RuntimeException("Parking space with number " + request.getSpaceNumber() + " already exists");
        }
        
        ParkingSpace parkingSpace = new ParkingSpace(
            request.getSpaceNumber(),
            request.getLocation(),
            request.getZone(),
            request.getCity(),
            request.getHourlyRate(),
            request.getOwnerId()
        );
        parkingSpace.setType(request.getType());
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public ParkingSpace updateParkingSpace(Long id, ParkingSpaceRequest request) {
        ParkingSpace existingSpace = getParkingSpaceById(id);
        
        // Check if new space number conflicts with existing ones (excluding current space)
        parkingSpaceRepository.findBySpaceNumber(request.getSpaceNumber())
            .ifPresent(space -> {
                if (!space.getId().equals(id)) {
                    throw new RuntimeException("Parking space with number " + request.getSpaceNumber() + " already exists");
                }
            });
        
        existingSpace.setSpaceNumber(request.getSpaceNumber());
        existingSpace.setLocation(request.getLocation());
        existingSpace.setZone(request.getZone());
        existingSpace.setCity(request.getCity());
        existingSpace.setType(request.getType());
        existingSpace.setHourlyRate(request.getHourlyRate());
        existingSpace.setOwnerId(request.getOwnerId());
        existingSpace.setLastUpdated(LocalDateTime.now());
        
        return parkingSpaceRepository.save(existingSpace);
    }
    
    public void deleteParkingSpace(Long id) {
        ParkingSpace parkingSpace = getParkingSpaceById(id);
        parkingSpaceRepository.delete(parkingSpace);
    }
    
    public List<ParkingSpace> getAvailableParkingSpaces() {
        return parkingSpaceRepository.findByStatus(SpaceStatus.AVAILABLE);
    }
    
    public List<ParkingSpace> getParkingSpacesByCity(String city) {
        return parkingSpaceRepository.findByCity(city);
    }
    
    public List<ParkingSpace> getParkingSpacesByZone(String zone) {
        return parkingSpaceRepository.findByZone(zone);
    }
    
    public List<ParkingSpace> getParkingSpacesByOwner(String ownerId) {
        return parkingSpaceRepository.findByOwnerId(ownerId);
    }
    
    public List<ParkingSpace> getAvailableParkingSpacesByCity(String city) {
        return parkingSpaceRepository.findByCityAndStatus(city, SpaceStatus.AVAILABLE);
    }
    
    public List<ParkingSpace> getAvailableParkingSpacesByZone(String zone) {
        return parkingSpaceRepository.findByZoneAndStatus(zone, SpaceStatus.AVAILABLE);
    }
    
    public ParkingSpace reserveParkingSpace(ReservationRequest request) {
        ParkingSpace parkingSpace = getParkingSpaceById(request.getSpaceId());
        
        if (parkingSpace.getStatus() != SpaceStatus.AVAILABLE) {
            throw new RuntimeException("Parking space is not available for reservation");
        }
        
        parkingSpace.setStatus(SpaceStatus.RESERVED);
        parkingSpace.setReservedBy(request.getUserId());
        parkingSpace.setReservationTime(LocalDateTime.now());
        parkingSpace.setLastUpdated(LocalDateTime.now());
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public ParkingSpace releaseParkingSpace(Long spaceId) {
        ParkingSpace parkingSpace = getParkingSpaceById(spaceId);
        
        parkingSpace.setStatus(SpaceStatus.AVAILABLE);
        parkingSpace.setReservedBy(null);
        parkingSpace.setReservationTime(null);
        parkingSpace.setLastUpdated(LocalDateTime.now());
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public ParkingSpace occupyParkingSpace(Long spaceId) {
        ParkingSpace parkingSpace = getParkingSpaceById(spaceId);
        
        if (parkingSpace.getStatus() != SpaceStatus.AVAILABLE && 
            parkingSpace.getStatus() != SpaceStatus.RESERVED) {
            throw new RuntimeException("Parking space is not available for occupation");
        }
        
        parkingSpace.setStatus(SpaceStatus.OCCUPIED);
        parkingSpace.setLastUpdated(LocalDateTime.now());
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public ParkingSpace updateSpaceStatus(Long spaceId, SpaceStatus status) {
        ParkingSpace parkingSpace = getParkingSpaceById(spaceId);
        parkingSpace.setStatus(status);
        parkingSpace.setLastUpdated(LocalDateTime.now());
        
        if (status == SpaceStatus.AVAILABLE) {
            parkingSpace.setReservedBy(null);
            parkingSpace.setReservationTime(null);
        }
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public Map<String, Object> getParkingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<ParkingSpace> allSpaces = parkingSpaceRepository.findAll();
        
        long totalSpaces = allSpaces.size();
        long availableSpaces = allSpaces.stream()
            .mapToLong(space -> space.getStatus() == SpaceStatus.AVAILABLE ? 1 : 0)
            .sum();
        long occupiedSpaces = allSpaces.stream()
            .mapToLong(space -> space.getStatus() == SpaceStatus.OCCUPIED ? 1 : 0)
            .sum();
        long reservedSpaces = allSpaces.stream()
            .mapToLong(space -> space.getStatus() == SpaceStatus.RESERVED ? 1 : 0)
            .sum();
        
        stats.put("totalSpaces", totalSpaces);
        stats.put("availableSpaces", availableSpaces);
        stats.put("occupiedSpaces", occupiedSpaces);
        stats.put("reservedSpaces", reservedSpaces);
        stats.put("occupancyRate", totalSpaces > 0 ? (double) occupiedSpaces / totalSpaces * 100 : 0);
        
        return stats;
    }
    
    public Map<String, Object> getParkingStatisticsByCity(String city) {
        Map<String, Object> stats = new HashMap<>();
        
        List<ParkingSpace> citySpaces = parkingSpaceRepository.findByCity(city);
        
        long totalSpaces = citySpaces.size();
        long availableSpaces = parkingSpaceRepository.countByCityAndStatus(city, SpaceStatus.AVAILABLE);
        long occupiedSpaces = parkingSpaceRepository.countByCityAndStatus(city, SpaceStatus.OCCUPIED);
        long reservedSpaces = parkingSpaceRepository.countByCityAndStatus(city, SpaceStatus.RESERVED);
        
        stats.put("city", city);
        stats.put("totalSpaces", totalSpaces);
        stats.put("availableSpaces", availableSpaces);
        stats.put("occupiedSpaces", occupiedSpaces);
        stats.put("reservedSpaces", reservedSpaces);
        stats.put("occupancyRate", totalSpaces > 0 ? (double) occupiedSpaces / totalSpaces * 100 : 0);
        
        return stats;
    }
}