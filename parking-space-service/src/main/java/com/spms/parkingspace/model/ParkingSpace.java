package com.spms.parkingspace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Space number is required")
    @Column(unique = true)
    private String spaceNumber;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotBlank(message = "Zone is required")
    private String zone;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @Enumerated(EnumType.STRING)
    private SpaceStatus status = SpaceStatus.AVAILABLE;
    
    @Enumerated(EnumType.STRING)
    private SpaceType type = SpaceType.REGULAR;
    
    @Positive(message = "Hourly rate must be positive")
    private Double hourlyRate;
    
    private String ownerId;
    
    private String reservedBy;
    
    private LocalDateTime reservationTime;
    
    private LocalDateTime lastUpdated = LocalDateTime.now();
    
    // Constructors
    public ParkingSpace() {}
    
    public ParkingSpace(String spaceNumber, String location, String zone, String city, 
                       Double hourlyRate, String ownerId) {
        this.spaceNumber = spaceNumber;
        this.location = location;
        this.zone = zone;
        this.city = city;
        this.hourlyRate = hourlyRate;
        this.ownerId = ownerId;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSpaceNumber() { return spaceNumber; }
    public void setSpaceNumber(String spaceNumber) { this.spaceNumber = spaceNumber; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public SpaceStatus getStatus() { return status; }
    public void setStatus(SpaceStatus status) { 
        this.status = status; 
        this.lastUpdated = LocalDateTime.now();
    }
    
    public SpaceType getType() { return type; }
    public void setType(SpaceType type) { this.type = type; }
    
    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    
    public String getReservedBy() { return reservedBy; }
    public void setReservedBy(String reservedBy) { this.reservedBy = reservedBy; }
    
    public LocalDateTime getReservationTime() { return reservationTime; }
    public void setReservationTime(LocalDateTime reservationTime) { this.reservationTime = reservationTime; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public enum SpaceStatus {
        AVAILABLE, OCCUPIED, RESERVED, OUT_OF_ORDER
    }
    
    public enum SpaceType {
        REGULAR, DISABLED, ELECTRIC, VIP
    }
}