/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */

// PaymentService.java
package com.spms.payment.service;

import com.spms.payment.dto.PaymentRequestDTO;
import com.spms.payment.dto.PaymentResponseDTO;
import com.spms.payment.dto.PaymentStatusUpdateDTO;
import com.spms.payment.model.Payment;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO getPaymentById(Long id);
    List<PaymentResponseDTO> getPaymentsByUser(Long userId);
    PaymentResponseDTO updatePaymentStatus(String paymentId, PaymentStatusUpdateDTO statusUpdateDTO);
    Payment getPaymentEntityByPaymentId(String paymentId);
}