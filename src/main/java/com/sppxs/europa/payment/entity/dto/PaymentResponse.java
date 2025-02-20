package com.sppxs.europa.payment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class PaymentResponse {
    private String status;
    private String extTransactionId;
    private String purchaseOrderId;
    private String transactionId;

    public PaymentResponse() {
    }

    public PaymentResponse(String status) {
        this.status = status;
    }
    public PaymentResponse(String status, String extTransactionId, String purchaseOrderId, String transactionId) {
        this.status = status;
        this.extTransactionId = extTransactionId;
        this.purchaseOrderId = purchaseOrderId;
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExtTransactionId() {
        return extTransactionId;
    }

    public void setExtTransactionId(String extTransactionId) {
        this.extTransactionId = extTransactionId;
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
}
