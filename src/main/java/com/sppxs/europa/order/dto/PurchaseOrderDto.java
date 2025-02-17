package com.sppxs.europa.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sppxs.europa.order.entity.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link PurchaseOrder}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderDto implements Serializable {
    Long id;
    String guid;
    List<PurchaseOrderItemDto> purchaseOrderItems;
    double amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List<PurchaseOrderItemDto> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public void setPurchaseOrderItems(List<PurchaseOrderItemDto> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}