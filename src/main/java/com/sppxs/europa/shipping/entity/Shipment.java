package com.sppxs.europa.shipping.entity;

import com.sppxs.europa.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipment")
public class Shipment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_details_id")
    private ShippingDetails shippingDetails;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private String instruction;
    private String status;
}
