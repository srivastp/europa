package com.sppxs.europa.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sppxs.europa.order.entity.Item;
import com.sppxs.europa.order.entity.PurchaseOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link PurchaseOrderItem}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderItemDto implements Serializable {
    NestedItemDto item;
    int quantity;

    public NestedItemDto getItem() {
        return item;
    }

    public void setItem(NestedItemDto item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * DTO for {@link Item}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NestedItemDto implements Serializable {
        Long sku;
        String name;
        double unitPrice;

        public Long getSku() {
            return sku;
        }

        public void setSku(Long sku) {
            this.sku = sku;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}