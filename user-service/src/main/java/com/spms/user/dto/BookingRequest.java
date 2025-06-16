package com.spms.user.dto;

import jakarta.validation.constraints.NotNull;

public class BookingRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Parking space ID is required")
    private Long parkingSpaceId;
    
    private Long vehicleId;
    
    private String notes;
    
    // Constructors
    public BookingRequest() {}
    
    public BookingRequest(Long userId, Long parkingSpaceId, Long vehicleId) {
        this.userId = userId;
        this.parkingSpaceId = parkingSpaceId;
        this.vehicleId = vehicleId;
    }
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getParkingSpaceId() { return parkingSpaceId; }
    public void setParkingSpaceId(Long parkingSpaceId) { this.parkingSpaceId = parkingSpaceId; }
    
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}