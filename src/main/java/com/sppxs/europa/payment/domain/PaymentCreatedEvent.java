package com.sppxs.europa.payment.domain;

import com.sppxs.europa.payment.entity.Transaction;
import lombok.Getter;


import java.math.BigDecimal;
import java.util.Set;

@Getter
//@AllArgsConstructor
public class PaymentCreatedEvent {
    private final String paymentId;
    private final Set<Transaction> transactionId;
    private final BigDecimal amount;

    public PaymentCreatedEvent(String paymentId, Set<Transaction> transactionId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.transactionId = transactionId;
        this.amount = amount;
    }
}
