package com.sppxs.europa.order.dto.mapper;

import com.sppxs.europa.order.dto.PurchaseOrderDto;
import com.sppxs.europa.order.entity.PurchaseOrder;


public interface PurchaseOrderMapper {
    PurchaseOrder toEntity(PurchaseOrderDto purchaseOrderDto);

    PurchaseOrderDto toDto(PurchaseOrder purchaseOrder);

    PurchaseOrder partialUpdate(PurchaseOrderDto purchaseOrderDto, PurchaseOrder purchaseOrder);
}