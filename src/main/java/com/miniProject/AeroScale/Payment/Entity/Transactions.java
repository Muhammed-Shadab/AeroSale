package com.miniProject.AeroScale.Payment.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Transactions {

    @Id
    @UuidGenerator
    private UUID id;


    @Column(updatable = false)
    private UUID orderId;

    @NotNull
    @Column(unique = true, updatable = false)
    private UUID RazorPayOrderId;

    @NotNull
    @Column(updatable = false)
    private BigDecimal amount;


    private String paymentMethod;

    @Column(unique = true)
    private String paymentId;
    private String signature;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    public enum Status{
        PENDING,
        FAILED,
        COMPLETED
    }
}
