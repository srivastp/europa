package com.sppxs.europa.store.service;

import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import com.sppxs.europa.order.enums.POStatus;
import com.sppxs.europa.order.repository.ItemRepository;
import com.sppxs.europa.order.repository.PurchaseOrderRepository;
import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.enums.PaymentStatus;
import com.sppxs.europa.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.sppxs.europa.order.enums.POStatus.*;

@Service
@Slf4j
public class StoreService {
    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    private final ItemRepository itemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PaymentService paymentService;

    public StoreService(ItemRepository itemRepository,
                        PurchaseOrderRepository purchaseOrderRepository,
                        PaymentService paymentService) {
        this.itemRepository = itemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.paymentService = paymentService;
    }

    public Long createOrder() {
        PurchaseOrder po = createPurchaseOrder();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Payment payment = paymentService.createPayment(po);
            logger.info("!! Payment created with id: " + payment.getId() + " | guid: " + payment.getPurchaseOrderId());
            if (payment.getStatus().equals(PaymentStatus.DECLINED)) {
                po.setStatus(POStatus.DECLINED);
            } else if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
                po.setStatus(READY_TO_SHIP);
            } else {
                po.setStatus(PENDING);
            }

            purchaseOrderRepository.save(po);

            logger.info("Thread_Id: " + Thread.currentThread().threadId() +
                    " | Order status updated to: " + po.getStatus());

        }).exceptionally(throwable -> {
            po.setStatus(SERVICE_ERROR);
            purchaseOrderRepository.save(po);
            logger.warn("Payment processing failed: " + throwable.getMessage());
            return null;
        });

        logger.info("!! Order created with id: " + po.getId() + " and status: " + po.getStatus());
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
        purchaseOrder.setUsername("milo");
        purchaseOrder.setCreatedAt(Instant.now());
        purchaseOrder.setUpdatedAt(Instant.now());
        purchaseOrder.setStatus(CREATED);

        int itemsSize = new Random().nextInt(4) + 1;
        for (int j = 0; j < itemsSize; j++) {
            PurchaseOrderItem i = new PurchaseOrderItem();
            //Get a random item from the item repository
            Long itemId = new Random().nextInt(5) + 1L;
            i.setItem(itemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalStateException("Item not found: " + itemId)));
            //Set a random quantity
            i.setQuantity(new Random().nextInt(5) + 1);
            i.setCreatedAt(Instant.now());
            i.setUpdatedAt(Instant.now());
            purchaseOrder.addOrderItem(i);
        }
        return purchaseOrderRepository.save(purchaseOrder);
    }
}
