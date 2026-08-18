package com.miniProject.AeroScale.BuyerModule.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BuyerAddress {

    @Id
    @UuidGenerator
    @Column(updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Buyer buyer;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private AddressLabel addressLabel = AddressLabel.HOME;


    @Column(length = 50)
    private String recipientName;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String recipientPhoneNo;

    @NotBlank
    @Column(nullable = false)
    private String addressLine1;

    @Size(max = 250)
    private String addressLine2;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String city;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String state;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String pincode;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String country;

    @Builder.Default
    @Column(nullable = false)
    private boolean isDefault = false;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public enum AddressLabel{
        HOME,WORK,OTHER
    }

    @PrePersist
    protected void createdAt() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void update() {
        updatedAt = Instant.now();
    }
}
