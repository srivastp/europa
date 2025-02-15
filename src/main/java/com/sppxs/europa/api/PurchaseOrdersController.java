package com.sppxs.europa.api;

import com.sppxs.europa.dto.PurchaseOrderDto;
import com.sppxs.europa.dto.mapper.PurchaseOrderMapper;
import com.sppxs.europa.entity.PurchaseOrder;
import com.sppxs.europa.entity.PurchaseOrderItem;
import com.sppxs.europa.service.PurchaseOrderService;
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
