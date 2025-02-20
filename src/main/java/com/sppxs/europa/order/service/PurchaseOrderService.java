package com.sppxs.europa.order.service;

import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import com.sppxs.europa.order.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrder createOrder(List<PurchaseOrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        for (PurchaseOrderItem orderItem : orderItems) {
            if (orderItem == null) {
                throw new IllegalArgumentException("Order item cannot be null");
            }
            purchaseOrder.addOrderItem(orderItem);
        }
        try {
            return purchaseOrderRepository.save(purchaseOrder);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create purchase order", e);
        }
    }

    public List<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll();
    }
}
