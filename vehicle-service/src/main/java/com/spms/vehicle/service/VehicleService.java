package com.spms.vehicle.service;

import com.spms.vehicle.dto.VehicleRequest;
import com.spms.vehicle.dto.VehicleEntryRequest;
import com.spms.vehicle.model.Vehicle;
import com.spms.vehicle.model.Vehicle.VehicleStatus;
import com.spms.vehicle.model.VehicleEntry;
import com.spms.vehicle.model.VehicleEntry.EntryStatus;
import com.spms.vehicle.repository.VehicleRepository;
import com.spms.vehicle.repository.VehicleEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private VehicleEntryRepository vehicleEntryRepository;
    
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }
    
    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate)
            .orElseThrow(() -> new RuntimeException("Vehicle not found with license plate: " + licensePlate));
    }
    
    public Vehicle createVehicle(VehicleRequest request) {
        // Check if license plate already exists
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new RuntimeException("Vehicle with license plate " + request.getLicensePlate() + " already exists");
        }
        
        Vehicle vehicle = new Vehicle(
            request.getLicensePlate(),
            request.getMake(),
            request.getModel(),
            request.getColor(),
            request.getYear(),
            request.getType(),
            request.getOwnerId()
        );
        
        return vehicleRepository.save(vehicle);
    }
    
    public Vehicle updateVehicle(Long id, VehicleRequest request) {
        Vehicle existingVehicle = getVehicleById(id);
        
        // Check if new license plate conflicts with existing ones (excluding current vehicle)
        vehicleRepository.findByLicensePlate(request.getLicensePlate())
            .ifPresent(vehicle -> {
                if (!vehicle.getId().equals(id)) {
                    throw new RuntimeException("Vehicle with license plate " + request.getLicensePlate() + " already exists");
                }
            });
        
        existingVehicle.setLicensePlate(request.getLicensePlate());
        existingVehicle.setMake(request.getMake());
        existingVehicle.setModel(request.getModel());
        existingVehicle.setColor(request.getColor());
        existingVehicle.setYear(request.getYear());
        existingVehicle.setType(request.getType());
        existingVehicle.setOwnerId(request.getOwnerId());
        existingVehicle.setUpdatedAt(LocalDateTime.now());
        
        return vehicleRepository.save(existingVehicle);
    }
    
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        
        // Check if vehicle is currently parked
        if (vehicle.getStatus() == VehicleStatus.PARKED_IN) {
            throw new RuntimeException("Cannot delete vehicle that is currently parked");
        }
        
        vehicleRepository.delete(vehicle);
    }
    
    public List<Vehicle> getVehiclesByOwner(String ownerId) {
        return vehicleRepository.findByOwnerId(ownerId);
    }
    
    public List<Vehicle> getVehiclesByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status);
    }
    
    public VehicleEntry simulateVehicleEntry(VehicleEntryRequest request) {
        Vehicle vehicle = getVehicleById(request.getVehicleId());
        
        // Check if vehicle is already parked
        if (vehicle.getStatus() == VehicleStatus.PARKED_IN) {
            throw new RuntimeException("Vehicle is already parked");
        }
        
        // Check if there's an active entry for this vehicle
        vehicleEntryRepository.findByVehicleIdAndStatus(vehicle.getId(), EntryStatus.ACTIVE)
            .ifPresent(entry -> {
                throw new RuntimeException("Vehicle already has an active parking session");
            });
        
        // Create new vehicle entry
        VehicleEntry entry = new VehicleEntry(request.getVehicleId(), request.getParkingSpaceId());
        entry = vehicleEntryRepository.save(entry);
        
        // Update vehicle status
        vehicle.setStatus(VehicleStatus.PARKED_IN);
        vehicle.setCurrentParkingSpaceId(request.getParkingSpaceId());
        vehicle.setEntryTime(entry.getEntryTime());
        vehicle.setExitTime(null);
        vehicleRepository.save(vehicle);
        
        return entry;
    }
    
    public VehicleEntry simulateVehicleExit(Long vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId);
        
        // Check if vehicle is currently parked
        if (vehicle.getStatus() != VehicleStatus.PARKED_IN) {
            throw new RuntimeException("Vehicle is not currently parked");
        }
        
        // Find active entry
        VehicleEntry entry = vehicleEntryRepository.findByVehicleIdAndStatus(vehicleId, EntryStatus.ACTIVE)
            .orElseThrow(() -> new RuntimeException("No active parking session found for vehicle"));
        
        // Calculate parking duration and amount (assuming $5/hour rate for simulation)
        LocalDateTime exitTime = LocalDateTime.now();
        long hoursParked = ChronoUnit.HOURS.between(entry.getEntryTime(), exitTime);
        if (hoursParked < 1) hoursParked = 1; // Minimum 1 hour charge
        
        double totalAmount = hoursParked * 5.0; // $5 per hour
        
        // Update entry
        entry.setExitTime(exitTime);
        entry.setStatus(EntryStatus.COMPLETED);
        entry.setTotalAmount(totalAmount);
        entry = vehicleEntryRepository.save(entry);
        
        // Update vehicle status
        vehicle.setStatus(VehicleStatus.PARKED_OUT);
        vehicle.setCurrentParkingSpaceId(null);
        vehicle.setExitTime(exitTime);
        vehicleRepository.save(vehicle);
        
        return entry;
    }
    
    public List<VehicleEntry> getVehicleEntryHistory(Long vehicleId) {
        return vehicleEntryRepository.findByVehicleIdOrderByEntryTimeDesc(vehicleId);
    }
    
    public List<VehicleEntry> getAllVehicleEntries() {
        return vehicleEntryRepository.findAll();
    }
    
    public VehicleEntry getVehicleEntryById(Long entryId) {
        return vehicleEntryRepository.findById(entryId)
            .orElseThrow(() -> new RuntimeException("Vehicle entry not found with id: " + entryId));
    }
    
    public List<VehicleEntry> getActiveVehicleEntries() {
        return vehicleEntryRepository.findByStatus(EntryStatus.ACTIVE);
    }
    
    public Map<String, Object> getVehicleStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<VehicleEntry> allEntries = vehicleEntryRepository.findAll();
        
        long totalVehicles = allVehicles.size();
        long parkedVehicles = allVehicles.stream()
            .mapToLong(vehicle -> vehicle.getStatus() == VehicleStatus.PARKED_IN ? 1 : 0)
            .sum();
        long activeEntries = vehicleEntryRepository.findByStatus(EntryStatus.ACTIVE).size();
        long completedEntries = vehicleEntryRepository.findByStatus(EntryStatus.COMPLETED).size();
        
        stats.put("totalVehicles", totalVehicles);
        stats.put("parkedVehicles", parkedVehicles);
        stats.put("vehiclesOut", totalVehicles - parkedVehicles);
        stats.put("activeEntries", activeEntries);
        stats.put("completedEntries", completedEntries);
        stats.put("totalEntries", allEntries.size());
        
        return stats;
    }
}