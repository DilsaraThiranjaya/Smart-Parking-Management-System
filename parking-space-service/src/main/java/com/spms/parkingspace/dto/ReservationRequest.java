package com.spms.parkingspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationRequest {
    
    @NotNull(message = "Space ID is required")
    private Long spaceId;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    // Constructors
    public ReservationRequest() {}
    
    public ReservationRequest(Long spaceId, String userId) {
        this.spaceId = spaceId;
        this.userId = userId;
    }
    
    // Getters and Setters
    public Long getSpaceId() { return spaceId; }
    public void setSpaceId(Long spaceId) { this.spaceId = spaceId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}