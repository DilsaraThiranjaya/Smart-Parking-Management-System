/**
 * @author supunmadhuranga
 * @created 2025-06-16
 * @project project
 */

package com.spms.payment.exception;

public class PaymentConflictException extends RuntimeException {
    public PaymentConflictException(String message) {
        super(message);
    }
}