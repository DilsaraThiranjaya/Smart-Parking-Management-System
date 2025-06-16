/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */

// PaymentRequestDTO.java
package com.spms.payment.dto;

import com.spms.payment.model.Payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    @NotBlank(message = "Payment ID is required")
    private String paymentId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Long parkingSpaceId;
    private Long vehicleId;

    @Positive(message = "Amount must be positive")
    private Double amount;

    private PaymentMethod method;
    private String cardNumber; // Last 4 digits
    private String cardHolderName;
    private String description;
}