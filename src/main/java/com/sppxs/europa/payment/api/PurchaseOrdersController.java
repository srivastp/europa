package com.sppxs.europa.payment.api;

import com.sppxs.europa.order.dto.PurchaseOrderDto;
import com.sppxs.europa.order.dto.mapper.PurchaseOrderMapper;
import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import com.sppxs.europa.order.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class PurchaseOrdersController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @GetMapping
    public ResponseEntity<List<PurchaseOrderDto>> getOrder() {
        List<PurchaseOrder> orders = purchaseOrderService.findAll();
        //Convert to DTO
        List<PurchaseOrderDto> orderDtos = orders
                .stream()
                .map(purchaseOrderMapper::toDto)
                .toList();
        return ResponseEntity.ok(orderDtos);
    }

    @PostMapping
    public PurchaseOrder createOrder(@RequestBody List<PurchaseOrderItem> orderItems) {
        return purchaseOrderService.createOrder(orderItems);
    }
}
