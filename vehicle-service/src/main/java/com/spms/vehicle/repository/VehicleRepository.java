package com.spms.vehicle.repository;

import com.spms.vehicle.model.Vehicle;
import com.spms.vehicle.model.Vehicle.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    List<Vehicle> findByOwnerId(String ownerId);
    
    List<Vehicle> findByStatus(VehicleStatus status);
    
    List<Vehicle> findByOwnerIdAndStatus(String ownerId, VehicleStatus status);
    
    List<Vehicle> findByCurrentParkingSpaceId(Long parkingSpaceId);
    
    boolean existsByLicensePlate(String licensePlate);
}