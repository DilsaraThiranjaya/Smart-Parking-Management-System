/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */


package com.spms.payment.service.impl;

import com.spms.payment.dto.ReceiptDTO;

import com.spms.payment.exception.InvalidReceiptRequestException;
import com.spms.payment.model.Payment;
import com.spms.payment.model.Payment.PaymentStatus;
import com.spms.payment.service.PaymentService;
import com.spms.payment.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final PaymentService paymentService;

    @Autowired
    public ReceiptServiceImpl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public ReceiptDTO generateReceipt(String paymentId) throws InvalidReceiptRequestException {
        Payment payment = paymentService.getPaymentEntityByPaymentId(paymentId);

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new InvalidReceiptRequestException(
                    "Cannot generate receipt for payment with status: " + payment.getStatus()
            );
        }

        return mapToReceiptDTO(payment);
    }

    private ReceiptDTO mapToReceiptDTO(Payment payment) {
        ReceiptDTO receipt = new ReceiptDTO();
        receipt.setReceiptNumber(payment.getPaymentId());
        receipt.setIssuedAt(payment.getProcessedAt());
        receipt.setUserId(payment.getUserId());
        receipt.setParkingSpaceId(payment.getParkingSpaceId());
        receipt.setVehicleId(payment.getVehicleId());
        receipt.setAmount(payment.getAmount());
        receipt.setMethod(payment.getMethod());

        if (payment.getCardNumber() != null && payment.getCardNumber().length() >= 4) {
            receipt.setCardLastFour(payment.getCardNumber().substring(payment.getCardNumber().length() - 4));
        }

        receipt.setTransactionId(payment.getTransactionId());
        receipt.setDescription(payment.getDescription());
        receipt.setStatus(payment.getStatus());

        // Custom receipt message
        StringBuilder note = new StringBuilder("Thank you for your payment. This receipt serves as confirmation of your transaction.");
        if (payment.getParkingSpaceId() != null) {
            note.append("\nParking Space ID: ").append(payment.getParkingSpaceId());
        }
        if (payment.getVehicleId() != null) {
            note.append("\nVehicle ID: ").append(payment.getVehicleId());
        }
        receipt.setReceiptNote(note.toString());

        return receipt;
    }
}