/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */

// PaymentRepository.java
package com.spms.payment.repository;

import com.spms.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByUserId(Long userId);
    boolean existsByPaymentId(String paymentId);
}