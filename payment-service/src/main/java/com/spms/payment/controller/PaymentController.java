package com.spms.payment.controller;

import com.spms.payment.entity.Payment;
import com.spms.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
        Optional<Payment> payment = paymentService.getPaymentByTransactionId(transactionId);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable Long userId) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Payment>> getPaymentsByVehicleId(@PathVariable Long vehicleId) {
        List<Payment> payments = paymentService.getPaymentsByVehicleId(vehicleId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/parking-space/{parkingSpaceId}")
    public ResponseEntity<List<Payment>> getPaymentsByParkingSpaceId(@PathVariable Long parkingSpaceId) {
        List<Payment> payments = paymentService.getPaymentsByParkingSpaceId(parkingSpaceId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/status/{paymentStatus}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable Payment.PaymentStatus paymentStatus) {
        List<Payment> payments = paymentService.getPaymentsByStatus(paymentStatus);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/method/{paymentMethod}")
    public ResponseEntity<List<Payment>> getPaymentsByMethod(@PathVariable Payment.PaymentMethod paymentMethod) {
        List<Payment> payments = paymentService.getPaymentsByMethod(paymentMethod);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/user/{userId}/status/{paymentStatus}")
    public ResponseEntity<List<Payment>> getUserPaymentsByStatus(
            @PathVariable Long userId, @PathVariable Payment.PaymentStatus paymentStatus) {
        List<Payment> payments = paymentService.getUserPaymentsByStatus(userId, paymentStatus);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<Payment>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Payment> payments = paymentService.getPaymentsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<Payment>> getUserPaymentsBetweenDates(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Payment> payments = paymentService.getUserPaymentsBetweenDates(userId, startDate, endDate);
        return ResponseEntity.ok(payments);
    }
    
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        try {
            Payment createdPayment = paymentService.createPayment(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/process")
    public ResponseEntity<Payment> processPayment(
            @PathVariable Long id, @RequestBody Map<String, String> cardDetails) {
        try {
            String cardNumber = cardDetails.get("cardNumber");
            String cardHolderName = cardDetails.get("cardHolderName");
            String expiryDate = cardDetails.get("expiryDate");
            String cvv = cardDetails.get("cvv");
            
            if (cardNumber == null || cardHolderName == null || expiryDate == null || cvv == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Payment processedPayment = paymentService.processPayment(id, cardNumber, cardHolderName, expiryDate, cvv);
            return ResponseEntity.ok(processedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id) {
        try {
            Payment refundedPayment = paymentService.refundPayment(id);
            return ResponseEntity.ok(refundedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Payment> cancelPayment(@PathVariable Long id) {
        try {
            Payment cancelledPayment = paymentService.cancelPayment(id);
            return ResponseEntity.ok(cancelledPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/stats/total-revenue")
    public ResponseEntity<Map<String, BigDecimal>> getTotalRevenue() {
        BigDecimal totalRevenue = paymentService.getTotalRevenue();
        return ResponseEntity.ok(Map.of("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO));
    }
    
    @GetMapping("/stats/user/{userId}/total-payments")
    public ResponseEntity<Map<String, BigDecimal>> getUserTotalPayments(@PathVariable Long userId) {
        BigDecimal totalPayments = paymentService.getUserTotalPayments(userId);
        return ResponseEntity.ok(Map.of("totalPayments", totalPayments != null ? totalPayments : BigDecimal.ZERO));
    }
    
    @GetMapping("/stats/completed-count")
    public ResponseEntity<Map<String, Long>> getCompletedPaymentsCount() {
        Long count = paymentService.getCompletedPaymentsCount();
        return ResponseEntity.ok(Map.of("completedPayments", count));
    }
    
    @GetMapping("/stats/pending-count")
    public ResponseEntity<Map<String, Long>> getPendingPaymentsCount() {
        Long count = paymentService.getPendingPaymentsCount();
        return ResponseEntity.ok(Map.of("pendingPayments", count));
    }
}