package com.spms.vehicle.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9]{2,10}$", message = "Invalid license plate format")
    @Column(unique = true)
    private String licensePlate;
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    private String color;
    
    private Integer year;
    
    @Enumerated(EnumType.STRING)
    private VehicleType type = VehicleType.CAR;
    
    @NotBlank(message = "Owner ID is required")
    private String ownerId;
    
    @Enumerated(EnumType.STRING)
    private VehicleStatus status = VehicleStatus.PARKED_OUT;
    
    private Long currentParkingSpaceId;
    
    private LocalDateTime entryTime;
    
    private LocalDateTime exitTime;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(String licensePlate, String make, String model, String color, 
                  Integer year, VehicleType type, String ownerId) {
        this.licensePlate = licensePlate;
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
        this.type = type;
        this.ownerId = ownerId;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
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
    
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public Long getCurrentParkingSpaceId() { return currentParkingSpaceId; }
    public void setCurrentParkingSpaceId(Long currentParkingSpaceId) { this.currentParkingSpaceId = currentParkingSpaceId; }
    
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
    
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public enum VehicleType {
        CAR, MOTORCYCLE, TRUCK, VAN, SUV, ELECTRIC
    }
    
    public enum VehicleStatus {
        PARKED_IN, PARKED_OUT, IN_TRANSIT
    }
}