package com.sppxs.europa.shipping.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shipping_details")
@EqualsAndHashCode(of = "id")
public class ShippingDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Version
    private Long version;

    //@NotNull
    //ToDo: @Index(name = "idx_shipping_details_po_id")
    @Column(name = "purchase_order_id", nullable = false)
    private String purchaseOrderId;
    @OneToMany(mappedBy = "shippingDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Shipment> shipments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Set<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(Set<Shipment> shipments) {
        this.shipments = shipments;
    }

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
