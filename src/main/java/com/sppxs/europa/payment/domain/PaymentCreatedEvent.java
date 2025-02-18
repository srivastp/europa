package com.sppxs.europa.payment.domain;

//@Getter
//@AllArgsConstructor
public class PaymentCreatedEvent {
    private final String paymentId;
    private final double amount;

    //Create a constructor with the purchaseOrderGuid and amount as parameters
    public PaymentCreatedEvent(String paymentId, double amount) {
        this.paymentId = paymentId;
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }
}
