package com.sppxs.europa.order.entity;

import com.sppxs.europa.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
public class Item extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long sku;

    @Column(nullable = false)
    private String name;

    private String description;

    private BigDecimal unitPrice;

    private int quantity;
}
