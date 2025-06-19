package com.spms.payment.service;

import com.spms.payment.entity.Payment;
import com.spms.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    private final Random random = new Random();
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
    
    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }
    
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
    
    public List<Payment> getPaymentsByVehicleId(Long vehicleId) {
        return paymentRepository.findByVehicleId(vehicleId);
    }
    
    public List<Payment> getPaymentsByParkingSpaceId(Long parkingSpaceId) {
        return paymentRepository.findByParkingSpaceId(parkingSpaceId);
    }
    
    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus paymentStatus) {
        return paymentRepository.findByPaymentStatus(paymentStatus);
    }
    
    public List<Payment> getPaymentsByMethod(Payment.PaymentMethod paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }
    
    public List<Payment> getUserPaymentsByStatus(Long userId, Payment.PaymentStatus paymentStatus) {
        return paymentRepository.findByUserIdAndPaymentStatus(userId, paymentStatus);
    }
    
    public List<Payment> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findPaymentsBetweenDates(startDate, endDate);
    }
    
    public List<Payment> getUserPaymentsBetweenDates(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findUserPaymentsBetweenDates(userId, startDate, endDate);
    }
    
    public Payment createPayment(Payment payment) {
        // Calculate amount based on duration and hourly rate
        BigDecimal calculatedAmount = payment.getHourlyRate()
                .multiply(BigDecimal.valueOf(payment.getParkingDurationHours()));
        payment.setAmount(calculatedAmount);
        
        // Set parking times
        payment.setParkingStartTime(LocalDateTime.now());
        payment.setParkingEndTime(LocalDateTime.now().plusHours(payment.getParkingDurationHours()));
        
        return paymentRepository.save(payment);
    }
    
    public Payment processPayment(Long paymentId, String cardNumber, String cardHolderName, 
                                 String expiryDate, String cvv) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        
        if (payment.getPaymentStatus() != Payment.PaymentStatus.PENDING) {
            throw new RuntimeException("Payment is not in pending status");
        }
        
        // Mock payment validation
        boolean isValidCard = validateMockCard(cardNumber, cardHolderName, expiryDate, cvv);
        
        if (isValidCard) {
            // Simulate payment processing
            boolean paymentSuccess = simulatePaymentProcessing();
            
            if (paymentSuccess) {
                payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
                payment.setPaymentDate(LocalDateTime.now());
                payment.setCardNumberMasked(maskCardNumber(cardNumber));
            } else {
                payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
            }
        } else {
            payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
        }
        
        return paymentRepository.save(payment);
    }
    
    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        
        if (payment.getPaymentStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }
        
        // Simulate refund processing
        boolean refundSuccess = simulateRefundProcessing();
        
        if (refundSuccess) {
            payment.setPaymentStatus(Payment.PaymentStatus.REFUNDED);
        } else {
            throw new RuntimeException("Refund processing failed");
        }
        
        return paymentRepository.save(payment);
    }
    
    public Payment cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        
        if (payment.getPaymentStatus() != Payment.PaymentStatus.PENDING) {
            throw new RuntimeException("Only pending payments can be cancelled");
        }
        
        payment.setPaymentStatus(Payment.PaymentStatus.CANCELLED);
        return paymentRepository.save(payment);
    }
    
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        
        if (payment.getPaymentStatus() == Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Cannot delete completed payment");
        }
        
        paymentRepository.delete(payment);
    }
    
    public BigDecimal getTotalRevenue() {
        return paymentRepository.getTotalAmountByStatus(Payment.PaymentStatus.COMPLETED);
    }
    
    public BigDecimal getUserTotalPayments(Long userId) {
        return paymentRepository.getTotalAmountByUserAndStatus(userId, Payment.PaymentStatus.COMPLETED);
    }
    
    public Long getCompletedPaymentsCount() {
        return paymentRepository.countByPaymentStatus(Payment.PaymentStatus.COMPLETED);
    }
    
    public Long getPendingPaymentsCount() {
        return paymentRepository.countByPaymentStatus(Payment.PaymentStatus.PENDING);
    }
    
    // Mock validation and processing methods
    private boolean validateMockCard(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        // Simple mock validation
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }
        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            return false;
        }
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        if (cvv == null || cvv.length() < 3 || cvv.length() > 4) {
            return false;
        }
        
        // Simulate card validation failure for testing (10% chance)
        return random.nextInt(10) != 0;
    }
    
    private boolean simulatePaymentProcessing() {
        // Simulate payment processing with 95% success rate
        return random.nextInt(100) < 95;
    }
    
    private boolean simulateRefundProcessing() {
        // Simulate refund processing with 98% success rate
        return random.nextInt(100) < 98;
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}