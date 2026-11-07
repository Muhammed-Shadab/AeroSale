package com.miniProject.AeroScale.Payment.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RazorPayOrderResponse {
    private String keyID;
    private int amount;
    private String orderId;
}
