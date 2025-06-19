package com.spms.parkingspace.service;

import com.spms.parkingspace.entity.ParkingSpace;
import com.spms.parkingspace.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParkingSpaceService {
    
    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;
    
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceRepository.findAll();
    }
    
    public Optional<ParkingSpace> getParkingSpaceById(Long id) {
        return parkingSpaceRepository.findById(id);
    }
    
    public Optional<ParkingSpace> getParkingSpaceByNumber(String spaceNumber) {
        return parkingSpaceRepository.findBySpaceNumber(spaceNumber);
    }
    
    public List<ParkingSpace> getAvailableParkingSpaces() {
        return parkingSpaceRepository.findByStatus(ParkingSpace.ParkingStatus.AVAILABLE);
    }
    
    public List<ParkingSpace> getParkingSpacesByCity(String city) {
        return parkingSpaceRepository.findByCity(city);
    }
    
    public List<ParkingSpace> getParkingSpacesByZone(String zone) {
        return parkingSpaceRepository.findByZone(zone);
    }
    
    public List<ParkingSpace> getParkingSpacesByOwner(Long ownerId) {
        return parkingSpaceRepository.findByOwnerId(ownerId);
    }
    
    public List<ParkingSpace> getAvailableSpacesByCityAndZone(String city, String zone) {
        return parkingSpaceRepository.findAvailableSpacesByCityAndZone(city, zone, ParkingSpace.ParkingStatus.AVAILABLE);
    }
    
    public ParkingSpace createParkingSpace(ParkingSpace parkingSpace) {
        // Check if space number already exists
        if (parkingSpaceRepository.findBySpaceNumber(parkingSpace.getSpaceNumber()).isPresent()) {
            throw new RuntimeException("Parking space with number " + parkingSpace.getSpaceNumber() + " already exists");
        }
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public ParkingSpace updateParkingSpace(Long id, ParkingSpace parkingSpaceDetails) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found with id: " + id));
        
        parkingSpace.setLocation(parkingSpaceDetails.getLocation());
        parkingSpace.setZone(parkingSpaceDetails.getZone());
        parkingSpace.setCity(parkingSpaceDetails.getCity());
        parkingSpace.setHourlyRate(parkingSpaceDetails.getHourlyRate());
        parkingSpace.setStatus(parkingSpaceDetails.getStatus());
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public ParkingSpace reserveParkingSpace(Long id, int durationHours) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found with id: " + id));
        
        if (parkingSpace.getStatus() != ParkingSpace.ParkingStatus.AVAILABLE) {
            throw new RuntimeException("Parking space is not available for reservation");
        }
        
        parkingSpace.setStatus(ParkingSpace.ParkingStatus.RESERVED);
        parkingSpace.setReservedUntil(LocalDateTime.now().plusHours(durationHours));
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public ParkingSpace releaseParkingSpace(Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found with id: " + id));
        
        parkingSpace.setStatus(ParkingSpace.ParkingStatus.AVAILABLE);
        parkingSpace.setReservedUntil(null);
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public ParkingSpace occupyParkingSpace(Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found with id: " + id));
        
        if (parkingSpace.getStatus() == ParkingSpace.ParkingStatus.OCCUPIED) {
            throw new RuntimeException("Parking space is already occupied");
        }
        
        parkingSpace.setStatus(ParkingSpace.ParkingStatus.OCCUPIED);
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    public void deleteParkingSpace(Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found with id: " + id));
        
        parkingSpaceRepository.delete(parkingSpace);
    }
    
    public Long getAvailableSpacesCount(String city) {
        return parkingSpaceRepository.countByCityAndStatus(city, ParkingSpace.ParkingStatus.AVAILABLE);
    }
}