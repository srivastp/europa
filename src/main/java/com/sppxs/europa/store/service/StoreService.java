package com.sppxs.europa.store.service;

import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import com.sppxs.europa.order.repository.ItemRepository;
import com.sppxs.europa.order.repository.PurchaseOrderRepository;
import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.repository.PaymentTypeDetailRepository;
import com.sppxs.europa.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class StoreService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private PaymentTypeDetailRepository paymentTypeDetailRepository;
    @Autowired
    private PaymentService paymentService;

    public Long createOrder() {
        PurchaseOrder po = createPurchaseOrder();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Payment payment = paymentService.createPayment(po);
            System.out.println("Payment created with id: " + payment.getId());
            if (payment.getStatus().equals("Declined")) {
                po.setStatus("Declined");
            } else if (payment.getStatus().equals("Success")) {
                po.setStatus("Ready to ship");
            } else {
                po.setStatus("Pending");
            }
            purchaseOrderRepository.save(po);
            System.out.println("Thread_Id: " + Thread.currentThread().threadId() +
                    " | Order status updated to: " + po.getStatus());
        }).exceptionally(throwable -> {
            po.setStatus("Failed");
            purchaseOrderRepository.save(po);
            System.err.println("Payment processing failed: " + throwable.getMessage());
            return null;
        });
        ;

        System.out.println("%%%% Order created with id: " + po.getId());
        return po.getId();
    }

    private PurchaseOrder createPurchaseOrder() {
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setGuid(UUID.randomUUID().toString());
        purchaseOrder.setUsername("jdoe");
        purchaseOrder.setStatus("Created");

        int itemsSize = new Random().nextInt(4) + 1;
        for (int j = 0; j < itemsSize; j++) {
            PurchaseOrderItem i = new PurchaseOrderItem();
            //Get a random item from the item repository
            Long itemId = new Random().nextInt(5) + 1L;
            i.setItem(itemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalStateException("Item not found: " + itemId)));
            //Set a random quantity
            i.setQuantity(new Random().nextInt(5) + 1);
            purchaseOrder.addOrderItem(i);
        }
        return purchaseOrderRepository.save(purchaseOrder);
    }
}
