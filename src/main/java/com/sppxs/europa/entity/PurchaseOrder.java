package com.sppxs.europa.entity;

import jakarta.persistence.*;

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

    private String guid;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();
    private double amount;

    public List<PurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public void setPurchaseOrderItems(List<PurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
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

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void addOrderItem(PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItems.add(purchaseOrderItem);
        purchaseOrderItem.setOrder(this);
        amount += purchaseOrderItem.getTotalPrice();
    }

    public void removeOrderItem(PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItems.remove(purchaseOrderItem);
        purchaseOrderItem.setOrder(null);
        amount -= purchaseOrderItem.getTotalPrice();
    }
}
