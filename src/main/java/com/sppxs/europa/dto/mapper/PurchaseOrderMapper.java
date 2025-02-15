package com.sppxs.europa.dto.mapper;

import com.sppxs.europa.dto.PurchaseOrderDto;
import com.sppxs.europa.entity.PurchaseOrder;


public interface PurchaseOrderMapper {
    PurchaseOrder toEntity(PurchaseOrderDto purchaseOrderDto);

    PurchaseOrderDto toDto(PurchaseOrder purchaseOrder);

    PurchaseOrder partialUpdate(PurchaseOrderDto purchaseOrderDto, PurchaseOrder purchaseOrder);
}