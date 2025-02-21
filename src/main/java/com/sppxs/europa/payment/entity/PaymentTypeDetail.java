package com.sppxs.europa.payment.entity;

import com.sppxs.europa.shared.entity.BaseEntity;
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
@Table(name = "payment_type_detail")
public class PaymentTypeDetail extends BaseEntity {

    //ToDO @Convert(converter = EncryptedStringConverter.class)
    //@Column(name = "card_number", length = 255)
    private String cardNumber;

    private String cardHolderName;

    //@Column(name = "expiry_date", length = 7)
    //@Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$")
    private String expiryDate;

    //ToDo: @Transient - Do not store CVV
    private String cvv;

    private String cardType;

    private String accountNumber;

    private String accountHolderName;

    private String routingNumber;

    private String bankName;
}
