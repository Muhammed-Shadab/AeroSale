package com.miniProject.AeroScale.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Orders implements Persistable<UUID> {

    @Id
    @Column(updatable = false)
    private UUID id;

    // ===== START: CHANGE FOR PRE-ASSIGNED ID (Persistable implementation) =====
    // Spring Data JPA ko batane ke liye ki pre-generated UUID hone par bhi ye new entity hai
    // Taaki save() call hone par Hibernate persist() kare aur conflict na aaye
    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
    // ===== END: CHANGE FOR PRE-ASSIGNED ID =====

    @Column(nullable = false, updatable = false)
    private UUID buyerId;


    // This is used for idempotency checks
    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private String idempotencyKey;

    // The Snapshot: Saves the address data inside the orders table permanently..required cause storing only addressId does not guarntee correctness cause the user can change the address of that uid...here it must be immutable as the order is an established contract

    @Embedded
    private OrderAddress shippingAddressSnapshot;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public enum OrderStatus {
        PENDING, CONFIRMED, CANCELLED
    }

    @PrePersist
    protected void init() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) status = OrderStatus.PENDING;
    }

    @PreUpdate
    protected void update() {
        updatedAt = Instant.now();
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }
}
