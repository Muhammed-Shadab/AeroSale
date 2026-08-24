package com.miniProject.AeroScale.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReservation {

    @Id
    @UuidGenerator
    @Column(updatable = false)
    private UUID id;


    @Column(nullable = false, updatable = false)
    private UUID productId;

    @Column(nullable = false, updatable = false)
    private UUID orderId;

    @Column(nullable = false, updatable = false)
    private Integer quantityLocked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    // The exact moment the lock expires and stock is returned to the pool
    @Column(nullable = false, updatable = false)
    private Instant expiresAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public enum ReservationStatus {
        PENDING,   // Waiting for payment
        CONFIRMED, // Payment succeeded, stock permanently deducted
        EXPIRED,   // 5 minutes passed, stock released
        CANCELLED  // Buyer manually cancelled order
    }

    @PrePersist
    protected void init() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) status = ReservationStatus.PENDING;
    }

    @PreUpdate
    protected void update() {
        updatedAt = Instant.now();
    }
}