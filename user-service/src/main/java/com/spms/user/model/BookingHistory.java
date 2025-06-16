package com.spms.user.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_history")
public class BookingHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long parkingSpaceId;
    
    private Long vehicleId;
    
    @Column(nullable = false)
    private LocalDateTime bookingTime;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.ACTIVE;
    
    private Double totalAmount;
    
    private String paymentId;
    
    private String notes;
    
    // Constructors
    public BookingHistory() {}
    
    public BookingHistory(Long userId, Long parkingSpaceId, Long vehicleId) {
        this.userId = userId;
        this.parkingSpaceId = parkingSpaceId;
        this.vehicleId = vehicleId;
        this.bookingTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getParkingSpaceId() { return parkingSpaceId; }
    public void setParkingSpaceId(Long parkingSpaceId) { this.parkingSpaceId = parkingSpaceId; }
    
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public enum BookingStatus {
        ACTIVE, COMPLETED, CANCELLED, EXPIRED
    }
}