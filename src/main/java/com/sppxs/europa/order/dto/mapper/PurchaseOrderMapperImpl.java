package com.sppxs.europa.order.dto.mapper;

import com.sppxs.europa.order.dto.PurchaseOrderDto;
import com.sppxs.europa.order.entity.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;
import java.util.stream.Collectors;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-10-02T00:24:08-0600",
        comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 21.0.2 (Amazon.com Inc.)"
)
@Component
public class PurchaseOrderMapperImpl implements PurchaseOrderMapper {
    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Override
    public PurchaseOrder toEntity(PurchaseOrderDto purchaseOrderDto) {
        if (purchaseOrderDto == null) {
            return null;
        }

        PurchaseOrder purchaseOrder = new PurchaseOrder();

        return purchaseOrder;
    }

    @Override
    public PurchaseOrderDto toDto(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) {
            return null;
        }
        PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
        purchaseOrderDto.setId(purchaseOrder.getId());
        purchaseOrderDto.setGuid(purchaseOrder.getGuid());
        purchaseOrderDto.setAmount(purchaseOrder.getAmount());
        purchaseOrderDto.setPurchaseOrderItems(
                purchaseOrder.getPurchaseOrderItems()
                        .stream()
                        .map(purchaseOrderItemMapper::toDto)
                        .collect(Collectors.toList()
                        )
        );
        return purchaseOrderDto;
    }

    @Override
    public PurchaseOrder partialUpdate(PurchaseOrderDto purchaseOrderDto, PurchaseOrder purchaseOrder) {
        if (purchaseOrderDto == null) {
            return purchaseOrder;
        }
        //ToDo: Implement partial update logic
        return purchaseOrder;
    }
}
