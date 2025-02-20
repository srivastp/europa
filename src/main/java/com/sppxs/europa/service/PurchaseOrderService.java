package com.sppxs.europa.service;

import com.sppxs.europa.entity.PurchaseOrder;
import com.sppxs.europa.entity.PurchaseOrderItem;
import com.sppxs.europa.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrder createOrder(List<PurchaseOrderItem> orderItems) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        for (PurchaseOrderItem orderItem : orderItems) {
            purchaseOrder.addOrderItem(orderItem);
        }
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public List<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll();
    }
}
