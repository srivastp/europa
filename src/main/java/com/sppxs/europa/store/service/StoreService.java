package com.sppxs.europa.store.service;

import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import com.sppxs.europa.order.repository.ItemRepository;
import com.sppxs.europa.order.repository.PurchaseOrderRepository;
import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.entity.PaymentTypeDetail;
import com.sppxs.europa.payment.entity.Transaction;
import com.sppxs.europa.payment.repository.PaymentRepository;
import com.sppxs.europa.payment.repository.PaymentTypeDetailRepository;
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
    private PaymentRepository paymentRepository;

    public Long createOrder() {

        PurchaseOrder po  = createPurchaseOrder();

        CompletableFuture.runAsync(() -> {
            Payment payment = createPayment(po);
            System.out.println("Payment created with id: " + payment.getId());
        });
        System.out.println("Order created with id: " + po.getId());
        //Payment payment = createPayment(po);
        return po.getId();
    }

    private PurchaseOrder createPurchaseOrder() {
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setGuid(UUID.randomUUID().toString());
        purchaseOrder.setUsername("fsinatra");
        purchaseOrder.setStatus("Created");

        int itemsSize = new Random().nextInt(3) + 1;
        for (int j = 0; j < itemsSize; j++) {
            PurchaseOrderItem i = new PurchaseOrderItem();
            //Get a random item from the item repository
            i.setItem(itemRepository.findById(new Random().nextInt(3) + 1L).get());
            //Set a random quantity
            i.setQuantity(new Random().nextInt(5) + 1);
            purchaseOrder.addOrderItem(i);
        }
        return purchaseOrderRepository.save(purchaseOrder);
    }

    private Payment createPayment(PurchaseOrder newPO) {
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PaymentTypeDetail paymentTypeDetail = new PaymentTypeDetail();
        paymentTypeDetail.setCardNumber("1234-5678-1234-5678");
        paymentTypeDetail.setCardType("VISA");
        paymentTypeDetail.setExpiryDate("12/2024");
        paymentTypeDetail.setCvv("123");
        paymentTypeDetail.setCardHolderName("Frank Sinatra");

        PaymentTypeDetail ptd1 = paymentTypeDetailRepository.save(paymentTypeDetail);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(UUID.randomUUID().toString());
        transaction1.setAmount(newPO.getAmount());
        transaction1.setType("CREDIT");
        transaction1.setPaymentTypeDetail(ptd1);
        transaction1.setStatus("Pending");

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPurchaseOrderId(newPO.getGuid());
        payment.setUsername(newPO.getUsername());
        payment.addTransaction(transaction1);
        payment.setStatus("Pending");

        return paymentRepository.save(payment);
    }
}
