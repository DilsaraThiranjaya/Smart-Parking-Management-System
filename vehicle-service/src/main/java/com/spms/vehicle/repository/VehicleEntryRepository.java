package com.spms.vehicle.repository;

import com.spms.vehicle.model.VehicleEntry;
import com.spms.vehicle.model.VehicleEntry.EntryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, Long> {
    
    List<VehicleEntry> findByVehicleId(Long vehicleId);
    
    List<VehicleEntry> findByParkingSpaceId(Long parkingSpaceId);
    
    List<VehicleEntry> findByStatus(EntryStatus status);
    
    Optional<VehicleEntry> findByVehicleIdAndStatus(Long vehicleId, EntryStatus status);
    
    List<VehicleEntry> findByVehicleIdOrderByEntryTimeDesc(Long vehicleId);
}