package com.sppxs.europa.payment.entity;

import com.sppxs.europa.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    private String transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "payment_type_detail_id")
    private PaymentTypeDetail paymentTypeDetail;

    //ToDo: @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private com.sppxs.europa.payment.enums.Transaction.TransactionType type;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private com.sppxs.europa.payment.enums.Transaction.TransactionStatus status;
}
