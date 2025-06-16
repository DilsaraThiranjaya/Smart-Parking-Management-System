/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */
// PaymentController.java
package com.spms.payment.controller;

import com.spms.payment.dto.*;
import com.spms.payment.exception.InvalidReceiptRequestException;
import com.spms.payment.exception.PaymentConflictException;
import com.spms.payment.exception.PaymentNotFoundException;
import com.spms.payment.service.PaymentService;
import com.spms.payment.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ReceiptService receiptService;

    @Autowired
    public PaymentController(PaymentService paymentService, ReceiptService receiptService) {
        this.paymentService = paymentService;
        this.receiptService = receiptService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO dto) {
        try {
            PaymentResponseDTO response = paymentService.createPayment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PaymentConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable("id") Long id) {
        try {
            PaymentResponseDTO response = paymentService.getPaymentById(id);
            return ResponseEntity.ok(response);
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public List<PaymentResponseDTO> getPaymentsByUser(@PathVariable("userId") Long userId) {
        return paymentService.getPaymentsByUser(userId);
    }

    @PatchMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable("paymentId") String paymentId,
            @Valid @RequestBody PaymentStatusUpdateDTO dto) {
        try {
            PaymentResponseDTO response = paymentService.updatePaymentStatus(paymentId, dto);
            return ResponseEntity.ok(response);
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{paymentId}/receipt")
    public ResponseEntity<ReceiptDTO> generateReceipt(@PathVariable("paymentId") String paymentId) {
        try {
            ReceiptDTO receipt = receiptService.generateReceipt(paymentId);
            return ResponseEntity.ok(receipt);
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidReceiptRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorReceipt(e.getMessage()));
        }
    }

    private ReceiptDTO createErrorReceipt(String errorMessage) {
        ReceiptDTO errorReceipt = new ReceiptDTO();
        errorReceipt.setReceiptNote("Error: " + errorMessage);
        return errorReceipt;
    }
}