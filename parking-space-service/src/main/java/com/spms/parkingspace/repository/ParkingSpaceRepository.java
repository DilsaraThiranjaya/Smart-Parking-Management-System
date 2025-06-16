package com.spms.parkingspace.repository;

import com.spms.parkingspace.model.ParkingSpace;
import com.spms.parkingspace.model.ParkingSpace.SpaceStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    
    List<ParkingSpace> findByStatus(SpaceStatus status);
    
    List<ParkingSpace> findByCity(String city);
    
    List<ParkingSpace> findByZone(String zone);
    
    List<ParkingSpace> findByOwnerId(String ownerId);
    
    List<ParkingSpace> findByCityAndZone(String city, String zone);
    
    List<ParkingSpace> findByCityAndStatus(String city, SpaceStatus status);
    
    List<ParkingSpace> findByZoneAndStatus(String zone, SpaceStatus status);
    
    Optional<ParkingSpace> findBySpaceNumber(String spaceNumber);
    
    @Query("SELECT COUNT(p) FROM ParkingSpace p WHERE p.city = :city AND p.status = :status")
    Long countByCityAndStatus(@Param("city") String city, @Param("status") SpaceStatus status);
    
    @Query("SELECT COUNT(p) FROM ParkingSpace p WHERE p.zone = :zone AND p.status = :status")
    Long countByZoneAndStatus(@Param("zone") String zone, @Param("status") SpaceStatus status);
    
    @Query("SELECT COUNT(p) FROM ParkingSpace p WHERE p.ownerId = :ownerId AND p.status = :status")
    Long countByOwnerIdAndStatus(@Param("ownerId") String ownerId, @Param("status") SpaceStatus status);
}