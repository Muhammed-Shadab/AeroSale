package com.miniProject.AeroScale.flashsale.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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
    @Column(nullable = false ,updatable = false)
    private UUID id;

    @Column(nullable = false ,updatable = false)
    private UUID productId;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxItemsPerUser = 4;

    private FlashSaleStatus status = FlashSaleStatus.SCHEDULED;

    public enum FlashSaleStatus {
        SCHEDULED , ACTIVE , ENDED , CANCELLED
    }




}
