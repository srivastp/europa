package com.sppxs.europa.dto.mapper;

import com.sppxs.europa.dto.PurchaseOrderDto;
import com.sppxs.europa.dto.PurchaseOrderItemDto;
import com.sppxs.europa.entity.PurchaseOrder;
import com.sppxs.europa.entity.PurchaseOrderItem;


public interface PurchaseOrderItemMapper {
    PurchaseOrderItem toEntity(PurchaseOrderItemDto purchaseOrderItemDto);

    PurchaseOrderItemDto toDto(PurchaseOrderItem purchaseOrderItem);

    PurchaseOrderItem partialUpdate(PurchaseOrderItemDto purchaseOrderItemDto, PurchaseOrderItem purchaseOrderItem);
}