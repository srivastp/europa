package com.sppxs.europa.shipping.entity;

import com.sppxs.europa.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipping_details")
public class ShippingDetails extends BaseEntity {

    @NotNull
    //ToDo: @Index(name = "idx_shipping_details_id")
    @Column(name = "purchase_order_id", nullable = false)
    private String purchaseOrderId;

    @OneToMany(mappedBy = "shippingDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Shipment> shipments = new HashSet<>();

    public void addShipment(Shipment shipment) {
        if (shipment == null) {
            return;
        }
        shipments.add(shipment);
        shipment.setShippingDetails(this);
    }

    public void removeShipment(Shipment shipment) {
        if (shipment == null) {
            return;
        }
        shipments.remove(shipment);
        shipment.setShippingDetails(null);
    }
}
