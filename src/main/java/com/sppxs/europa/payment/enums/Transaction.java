package com.sppxs.europa.payment.enums;

public class Transaction {
    public enum TransactionType {
        DEBIT, CREDIT, REFUND, CHARGEBACK
    }

    public enum TransactionStatus {
        CREATED, SUCCESS, DECLINED, PENDING, APPROVED, SERVICE_ERROR, CANCELLED
    }
}
