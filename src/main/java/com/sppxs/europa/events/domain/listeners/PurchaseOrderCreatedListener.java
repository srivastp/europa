package com.sppxs.europa.events.domain.listeners;

import com.sppxs.europa.events.domain.PurchaseOrderCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderCreatedListener {

    @EventListener
    public void handleMyDomainEvent(PurchaseOrderCreatedEvent event) {
        System.out.println("PurchaseOrderCreatedEvent - Guid: " + event.getPurchaseOrderGuid() + " | Amount: " + event.getAmount());
        //Asynchronously send an email to the user
        //Asynchronously send a SQS message notification
    }
}
