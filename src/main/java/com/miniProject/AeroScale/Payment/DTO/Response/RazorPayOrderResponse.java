package com.miniProject.AeroScale.Payment.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RazorPayOrderResponse {
    private String keyID;
    private int amount;
    private String orderId;
}
