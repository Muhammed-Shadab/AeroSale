package com.miniProject.AeroScale.Payment.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStatus {
    @NotNull(message = "orderId cannot be null")
    private String rzpOrderId;

    private String paymentId;

    private String signature;
}