package com.sppxs.europa.order.dto.mapper;

import com.sppxs.europa.order.dto.PurchaseOrderItemDto;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-10-02T00:24:08-0600",
        comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 21.0.2 (Amazon.com Inc.)"
)
@Component
public class PurchaseOrderItemMapperImpl implements PurchaseOrderItemMapper {

    @Override
    public PurchaseOrderItem toEntity(PurchaseOrderItemDto purchaseOrderItemDto) {
        if (purchaseOrderItemDto == null) {
            return null;
        }

        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();

        return purchaseOrderItem;
    }

    @Override
    public PurchaseOrderItemDto toDto(PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItem == null) {
            return null;
        }
        PurchaseOrderItemDto.NestedItemDto nestedPOItemDto = new PurchaseOrderItemDto.NestedItemDto();
        nestedPOItemDto.setSku(purchaseOrderItem.getItem().getSku());
        nestedPOItemDto.setName(purchaseOrderItem.getItem().getName());
        nestedPOItemDto.setUnitPrice(purchaseOrderItem.getItem().getUnitPrice());

        PurchaseOrderItemDto purchaseOrderItemDto = new PurchaseOrderItemDto();
        purchaseOrderItemDto.setItem(nestedPOItemDto);
        purchaseOrderItemDto.setQuantity(purchaseOrderItem.getQuantity());

        return purchaseOrderItemDto;
    }

    @Override
    public PurchaseOrderItem partialUpdate(PurchaseOrderItemDto purchaseOrderItemDto, PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItemDto == null) {
            return purchaseOrderItem;
        }
        //ToDo: Implement partial update logic
        return purchaseOrderItem;
    }
}
