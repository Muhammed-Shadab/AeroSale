package com.miniProject.AeroScale.BuyerModule.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BuyerProfileResponse {
    private UUID id;
    private String fullName;
    private String email;
    private Instant dob;
    private BigDecimal walletBalance;
    private Long loyaltyPoint;
}
