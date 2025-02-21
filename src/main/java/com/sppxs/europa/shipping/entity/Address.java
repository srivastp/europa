package com.sppxs.europa.shipping.entity;

import com.sppxs.europa.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address extends BaseEntity {
    private String type;

    //@Enumerated(EnumType.STRING)
    @Column(length = 20)
    private String line1;

    private String line2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
