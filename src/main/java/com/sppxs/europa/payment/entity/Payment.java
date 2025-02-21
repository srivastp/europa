package com.sppxs.europa.payment.entity;

import com.sppxs.europa.payment.domain.PaymentCreatedEvent;
import com.sppxs.europa.payment.enums.PaymentStatus;
import com.sppxs.europa.shared.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    private String paymentId;

    private String purchaseOrderId;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

    private String username;

    private BigDecimal amount = BigDecimal.ZERO;

    private PaymentStatus status;

    @DomainEvents
    public List<Object> getDomainEvents() {
        return List.of(new PaymentCreatedEvent(this.paymentId, this.transactions, this.amount));
    }

    @AfterDomainEventPublication
    public void clearDomainEvents() {
        System.out.println(">> Payment events  published !!");
    }

    public void addTransaction(@NotNull Transaction transaction) {
        if (transaction.getAmount().equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Transaction amount cannot be negative");
        }
        transactions.add(transaction);
        transaction.setPayment(this);
        amount = amount.add(transaction.getAmount());
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setPayment(null);
        amount = amount.subtract(transaction.getAmount());
    }

    public void addTransactions(Set<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            addTransaction(transaction);
        }
    }

    public void removeTransactions(Set<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            removeTransaction(transaction);
        }
    }

}
