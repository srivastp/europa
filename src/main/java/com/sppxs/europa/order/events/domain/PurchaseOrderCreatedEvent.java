package com.sppxs.europa.order.events.domain;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
//@AllArgsConstructor
public class PurchaseOrderCreatedEvent {

    private final String purchaseOrderGuid;

    private final BigDecimal amount;

    //Create a constructor with the purchaseOrderGuid and amount as parameters
    public PurchaseOrderCreatedEvent(String purchaseOrderGuid, BigDecimal amount) {
        this.purchaseOrderGuid = purchaseOrderGuid;
        this.amount = amount;
    }
}
