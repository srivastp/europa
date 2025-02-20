package com.sppxs.europa.order.entity;

import com.sppxs.europa.order.events.domain.PurchaseOrderCreatedEvent;
import jakarta.persistence.*;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "guid", nullable = false, unique = true)
    //@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String guid;
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();
    private double amount;

    private String username;
    private String status;

    /*
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
    */

    public enum OrderStatus {
        CREATED, DECLINED, READY_TO_SHIP, PENDING
    }


    //@Transient
    //private final List<Object> purchaseOrderDomainEvents = new ArrayList<>();

    @DomainEvents
    public List<Object> getDomainEvents() {
        return List.of(new PurchaseOrderCreatedEvent(this.guid, this.amount));
    }

    @AfterDomainEventPublication
    public void clearDomainEvents() {
        System.out.println(">> All events published !!");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public double getAmount() {
        return amount;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addOrderItem(PurchaseOrderItem purchaseOrderItem) {
        try {
            purchaseOrderItems.add(purchaseOrderItem);
            purchaseOrderItem.setOrder(this);
            amount = purchaseOrderItem.getTotalPrice();
        } catch (Exception e) {
            purchaseOrderItems.remove(purchaseOrderItem);
            purchaseOrderItem.setOrder(null);
            throw e;
        }
    }

    public void removeOrderItem(PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItems.remove(purchaseOrderItem);
        purchaseOrderItem.setOrder(null);
        amount -= purchaseOrderItem.getTotalPrice();
    }
}
