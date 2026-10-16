package com.miniProject.AeroScale.SellerModule.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String id;
    private String email;
    private String bussinessName;
    private String bussinessAddress;
    private String wareHousePinCode;
    private Instant dob;
    private String verificationStatus;
    private BigDecimal rating;
    private long totalOrdersFullFilled;
    private BigDecimal totalRevenue;
    private Integer maxConcurrentFlashSaleSlots;
    private Instant createdAt;
}
