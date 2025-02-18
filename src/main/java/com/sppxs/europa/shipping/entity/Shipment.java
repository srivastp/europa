package com.sppxs.europa.shipping.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "shipment")
public class Shipment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_details_id")
    private ShippingDetails shippingDetails;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressInfo addressInfo;
    private String instruction;
    private String status;

    //private List<Item> items;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingDetails getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public AddressInfo getAddress() {
        return addressInfo;
    }

    public void setAddress(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
