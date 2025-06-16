/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */

// PaymentServiceImpl.java
package com.spms.payment.service.impl;

import com.spms.payment.dto.PaymentRequestDTO;
import com.spms.payment.dto.PaymentResponseDTO;
import com.spms.payment.dto.PaymentStatusUpdateDTO;

import com.spms.payment.exception.PaymentConflictException;
import com.spms.payment.exception.PaymentNotFoundException;
import com.spms.payment.model.Payment;
import com.spms.payment.model.Payment.PaymentMethod;
import com.spms.payment.model.Payment.PaymentStatus;
import com.spms.payment.repository.PaymentRepository;
import com.spms.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        if (paymentRepository.existsByPaymentId(dto.getPaymentId())) {
            throw new PaymentConflictException("Payment with ID " + dto.getPaymentId() + " already exists");
        }

        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setUserId(dto.getUserId());
        payment.setParkingSpaceId(dto.getParkingSpaceId());
        payment.setVehicleId(dto.getVehicleId());
        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());
        payment.setCardNumber(dto.getCardNumber());
        payment.setCardHolderName(dto.getCardHolderName());
        payment.setDescription(dto.getDescription());

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponseDTO(savedPayment);
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + id));
        return mapToResponseDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByUser(Long userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(String paymentId, PaymentStatusUpdateDTO dto) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));

        payment.setStatus(dto.getStatus());
        payment.setTransactionId(dto.getTransactionId());
        payment.setFailureReason(dto.getFailureReason());
        payment.setProcessedAt(LocalDateTime.now());

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponseDTO(updatedPayment);
    }

    @Override
    public Payment getPaymentEntityByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
    }

    private PaymentResponseDTO mapToResponseDTO(Payment payment) {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setId(payment.getId());
        responseDTO.setPaymentId(payment.getPaymentId());
        responseDTO.setUserId(payment.getUserId());
        responseDTO.setParkingSpaceId(payment.getParkingSpaceId());
        responseDTO.setVehicleId(payment.getVehicleId());
        responseDTO.setAmount(payment.getAmount());
        responseDTO.setStatus(payment.getStatus());
        responseDTO.setMethod(payment.getMethod());
        responseDTO.setCardNumber(payment.getCardNumber());
        responseDTO.setCardHolderName(payment.getCardHolderName());
        responseDTO.setTransactionId(payment.getTransactionId());
        responseDTO.setDescription(payment.getDescription());
        responseDTO.setCreatedAt(payment.getCreatedAt());
        responseDTO.setProcessedAt(payment.getProcessedAt());
        responseDTO.setFailureReason(payment.getFailureReason());
        return responseDTO;
    }
}