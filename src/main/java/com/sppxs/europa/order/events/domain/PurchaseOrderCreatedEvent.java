package com.sppxs.europa.order.events.domain;

//@Getter
//@AllArgsConstructor
public class PurchaseOrderCreatedEvent {
    private final String purchaseOrderGuid;
    private final double amount;

    //Create a constructor with the purchaseOrderGuid and amount as parameters
    public PurchaseOrderCreatedEvent(String purchaseOrderGuid, double amount) {
        this.purchaseOrderGuid = purchaseOrderGuid;
        this.amount = amount;
    }

    public String getPurchaseOrderGuid() {
        return purchaseOrderGuid;
    }

    public double getAmount() {
        return amount;
    }
}
