package com.miniProject.AeroScale.AuthModule.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Seller {

    @Id
    @Column(updatable = false)
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    private Users users;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String bussinessName;

    @Column(unique = true,length = 20)
    private String gstin;

    @Column(length = 150)
    private String bussinessAdress;

    @Column(length = 50)
    private String wareHousePinCode;

    @Column(nullable = false)
    private Instant DateOfBirth;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private SellerVerificationStatus sellerVerificationStatus = SellerVerificationStatus.PENDING;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal rating = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private Long totalOrdersFullFilled = 0L;

    @Builder.Default
    @Column(nullable = false)
    private int maxConcurrentFlashSaleSlots = 5;

    @Column(length = 34)
    private String BankAccountNumber;

    @Column(length = 20)
    private String BankIfscCode;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public enum SellerVerificationStatus {
        PENDING, VERIFIED, REJECTED, SUSPENDED
    }

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
