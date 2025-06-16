/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */

// ReceiptService.java
package com.spms.payment.service;

import com.spms.payment.dto.ReceiptDTO;
import com.spms.payment.exception.InvalidReceiptRequestException;

public interface ReceiptService {
    ReceiptDTO generateReceipt(String paymentId) throws InvalidReceiptRequestException;
}