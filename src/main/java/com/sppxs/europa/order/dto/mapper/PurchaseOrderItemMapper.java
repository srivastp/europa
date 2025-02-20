package com.sppxs.europa.order.dto.mapper;

import com.sppxs.europa.order.dto.PurchaseOrderItemDto;
import com.sppxs.europa.order.entity.PurchaseOrderItem;


public interface PurchaseOrderItemMapper {
    PurchaseOrderItem toEntity(PurchaseOrderItemDto purchaseOrderItemDto);

    PurchaseOrderItemDto toDto(PurchaseOrderItem purchaseOrderItem);

    PurchaseOrderItem partialUpdate(PurchaseOrderItemDto purchaseOrderItemDto, PurchaseOrderItem purchaseOrderItem);
}