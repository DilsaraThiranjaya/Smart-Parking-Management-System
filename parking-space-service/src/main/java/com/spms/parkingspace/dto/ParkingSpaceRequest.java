package com.spms.parkingspace.dto;

import com.spms.parkingspace.model.ParkingSpace.SpaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ParkingSpaceRequest {
    
    @NotBlank(message = "Space number is required")
    private String spaceNumber;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotBlank(message = "Zone is required")
    private String zone;
    
    @NotBlank(message = "City is required")
    private String city;
    
    private SpaceType type = SpaceType.REGULAR;
    
    @Positive(message = "Hourly rate must be positive")
    private Double hourlyRate;
    
    @NotBlank(message = "Owner ID is required")
    private String ownerId;
    
    // Constructors
    public ParkingSpaceRequest() {}
    
    // Getters and Setters
    public String getSpaceNumber() { return spaceNumber; }
    public void setSpaceNumber(String spaceNumber) { this.spaceNumber = spaceNumber; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public SpaceType getType() { return type; }
    public void setType(SpaceType type) { this.type = type; }
    
    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
}