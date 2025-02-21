package com.sppxs.europa.order.entity;

import com.sppxs.europa.order.enums.POStatus;
import com.sppxs.europa.order.events.domain.PurchaseOrderCreatedEvent;
import com.sppxs.europa.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder extends BaseEntity {

    @Column(name = "guid", nullable = false, unique = true)
    @NotBlank(message = "guid cannot be empty")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String guid;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();

    private BigDecimal amount = BigDecimal.ZERO;

    private String username;

    private POStatus status;

    //@Transient
    //private final List<Object> purchaseOrderDomainEvents = new ArrayList<>();

    @DomainEvents
    public List<Object> getDomainEvents() {
        return List.of(new PurchaseOrderCreatedEvent(this.guid, this.amount));
    }

    @AfterDomainEventPublication
    public void clearDomainEvents() {
        System.out.println(">> PO events published !!");
    }

    public void addOrderItem(@NotNull PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItems.add(purchaseOrderItem);
        purchaseOrderItem.setPurchaseOrder(this);
        amount = amount.add(purchaseOrderItem.getTotalPrice());
    }

    public void removeOrderItem(PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItems.remove(purchaseOrderItem);
        purchaseOrderItem.setPurchaseOrder(null);
        amount = amount.subtract(purchaseOrderItem.getTotalPrice());
    }
}
