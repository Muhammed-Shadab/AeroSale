package com.miniProject.AeroScale.Payment.DTO.Response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderResponse {
    private String keyID;
    private int amount;
    private String orderId;
}
