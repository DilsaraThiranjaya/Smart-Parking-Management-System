package com.spms.vehicle.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_entries")
public class VehicleEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long vehicleId;
    
    @Column(nullable = false)
    private Long parkingSpaceId;
    
    @Column(nullable = false)
    private LocalDateTime entryTime;
    
    private LocalDateTime exitTime;
    
    @Enumerated(EnumType.STRING)
    private EntryStatus status = EntryStatus.ACTIVE;
    
    private Double totalAmount;
    
    private String paymentId;
    
    // Constructors
    public VehicleEntry() {}
    
    public VehicleEntry(Long vehicleId, Long parkingSpaceId) {
        this.vehicleId = vehicleId;
        this.parkingSpaceId = parkingSpaceId;
        this.entryTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    
    public Long getParkingSpaceId() { return parkingSpaceId; }
    public void setParkingSpaceId(Long parkingSpaceId) { this.parkingSpaceId = parkingSpaceId; }
    
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
    
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    
    public EntryStatus getStatus() { return status; }
    public void setStatus(EntryStatus status) { this.status = status; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public enum EntryStatus {
        ACTIVE, COMPLETED, CANCELLED
    }
}