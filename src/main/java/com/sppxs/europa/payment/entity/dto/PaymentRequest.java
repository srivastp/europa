package com.sppxs.europa.payment.entity.dto;

import lombok.*;

/*@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)*/
public class PaymentRequest {
    private String purchaseOrderId;
    private String transactionId;

    public PaymentRequest() {
    }

    public PaymentRequest(String purchaseOrderId, String transactionId) {
        this.purchaseOrderId = purchaseOrderId;
        this.transactionId = transactionId;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    //private double amount;
}
