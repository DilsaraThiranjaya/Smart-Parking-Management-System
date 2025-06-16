package com.spms.vehicle.dto;

import jakarta.validation.constraints.NotNull;

public class VehicleEntryRequest {
    
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    
    @NotNull(message = "Parking space ID is required")
    private Long parkingSpaceId;
    
    // Constructors
    public VehicleEntryRequest() {}
    
    public VehicleEntryRequest(Long vehicleId, Long parkingSpaceId) {
        this.vehicleId = vehicleId;
        this.parkingSpaceId = parkingSpaceId;
    }
    
    // Getters and Setters
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    
    public Long getParkingSpaceId() { return parkingSpaceId; }
    public void setParkingSpaceId(Long parkingSpaceId) { this.parkingSpaceId = parkingSpaceId; }
}