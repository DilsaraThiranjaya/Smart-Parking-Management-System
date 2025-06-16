package com.spms.vehicle.dto;

import com.spms.vehicle.model.Vehicle.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VehicleRequest {
    
    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9]{2,10}$", message = "Invalid license plate format")
    private String licensePlate;
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    private String color;
    
    private Integer year;
    
    private VehicleType type = VehicleType.CAR;
    
    @NotBlank(message = "Owner ID is required")
    private String ownerId;
    
    // Constructors
    public VehicleRequest() {}
    
    // Getters and Setters
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }
    
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
}