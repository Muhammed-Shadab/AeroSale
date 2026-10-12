package com.miniProject.AeroScale.flashsale.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flash_sales")
public class FlashSale {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private UUID productId;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxItemsPerUser = 4;

    // sale ke dauran buyer jo price dega
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salePrice;

    // is sale ke liye alag rakhi gayi units
    @Column(nullable = false)
    private Integer totalQuantity;

    // confirm hui (paid) units. "82% claimed" bar isi se chalta hai
    @Column(nullable = false)
    @Builder.Default
    private Integer soldQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private FlashSaleStatus status = FlashSaleStatus.SCHEDULED;

    public enum FlashSaleStatus {
        SCHEDULED, ACTIVE, ENDED, CANCELLED
    }
}