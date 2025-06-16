/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */

// ReceiptDTO.java
package com.spms.payment.dto;

import com.spms.payment.model.Payment.PaymentMethod;
import com.spms.payment.model.Payment.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceiptDTO {
    private String receiptNumber;  // Same as paymentId
    private LocalDateTime issuedAt;
    private Long userId;
    private Long parkingSpaceId;
    private Long vehicleId;
    private Double amount;
    private PaymentMethod method;
    private String cardLastFour;
    private String transactionId;
    private String description;
    private PaymentStatus status;

    // Additional receipt-specific fields
    private String receiptNote;
    private String merchantName = "Smart Parking Management";
    private String merchantAddress = "123 Parking St, City";
    private String merchantTaxId = "TAX-12345";
}