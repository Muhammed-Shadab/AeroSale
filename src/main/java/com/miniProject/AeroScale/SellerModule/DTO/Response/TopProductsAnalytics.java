package com.miniProject.AeroScale.SellerModule.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TopProductsAnalytics {
    private String productId;
    private String imageUrl;
    private String name;
    private long unitsSold;
    private BigDecimal revenue;
}
