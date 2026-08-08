package com.miniProject.AeroScale.Entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.profile.Fetch;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Buyer {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Users users;


    @NotBlank
    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(length = 500)
    private String defaultShippingAddress;

    @Column(length = 20)
    private String defaultShippingPinCode;

    @Builder.Default
    @Column(precision = 12, scale = 2)
    private BigDecimal walletBalance = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private Long loyaltyPoint = 0L;

    @Builder.Default
    @Column(nullable = false)
    private boolean flagForAbuse = false;

    @Column(nullable = false)
    private Instant DateOfBirth;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void init() {
        Instant instant = Instant.now();
        createdAt = instant;
        updatedAt = instant;
    }

    @PreUpdate
    protected  void update() {
        updatedAt = Instant.now();
    }


}
