package com.miniProject.AeroScale.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.security.ProtectionDomain;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @UuidGenerator
    @Column(updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Orders order;

    @Column(nullable = false, updatable = false)
    private UUID productId;

    @Column(nullable = false, updatable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2, updatable = false)
    private BigDecimal unitPriceAtPurchase;

    // field for the pre-calculated subtotal of price*quantity
    @Column(nullable = false, precision = 12, scale = 2, updatable = false)
    private BigDecimal subTotal;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Boolean isFlashSaleProduct = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderItemStatus status = OrderItemStatus.PENDING;

    public enum OrderItemStatus {
        PENDING, CONFIRMED, CANCELLED, SHIPPED, DELIVERED
    }

    @PrePersist
    public void created() {
        this.createdAt = Instant.now();
    }


}