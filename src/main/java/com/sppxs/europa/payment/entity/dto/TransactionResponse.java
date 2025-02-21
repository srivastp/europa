package com.sppxs.europa.payment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.sppxs.europa.payment.enums.Transaction.TransactionStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private TransactionStatus status;
    private String extTransactionId;
    private String purchaseOrderId;
    private String transactionId;
}
