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
public class Inventory {

    @Id
    @UuidGenerator
    @Column(updatable = false)
    private UUID id;


    @Column(nullable = false, updatable = false, unique = true)
    private UUID productId;

    // The absolute physical amount sitting in the warehouse
    @Column(nullable = false)
    private Integer totalStock;

    // The amount currently sitting in buyers' carts during checkout -- ye use hoga locking keliye
    @Builder.Default
    @Column(nullable = false)
    private Integer lockedStock = 0;

    // Concurrency Lock: product se yaha move kiya hai ise..
    @Version
    private Long version;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void init() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (lockedStock == null) lockedStock = 0;
    }

    @PreUpdate
    protected void update() {
        updatedAt = Instant.now();
    }

    // Helper method to calculate what is actually buyable
    public Integer getAvailableStock() {
        return this.totalStock - this.lockedStock;
    }
}