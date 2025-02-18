package com.sppxs.europa.payment.entity;

import com.sppxs.europa.order.events.domain.PurchaseOrderCreatedEvent;
import com.sppxs.europa.payment.domain.PaymentCreatedEvent;
import jakarta.persistence.*;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String paymentId;

    private String purchaseOrderId;
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

    private String username;

    private double amount;

    private String status;

    @DomainEvents
    public List<Object> getDomainEvents() {
        return List.of(new PaymentCreatedEvent(this.paymentId, this.amount));
    }

    @AfterDomainEventPublication
    public void clearDomainEvents() {
        System.out.println(">> All payments published !!");
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setPayment(this);
        amount += transaction.getAmount();
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setPayment(null);
        amount -= transaction.getAmount();
    }
}
