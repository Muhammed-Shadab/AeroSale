package com.miniProject.AeroScale.SellerModule.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalyticsSummary {
     private BigDecimal revenue;
     private int orders;
     private long unitsSold;
     private BigDecimal previousRevenue;
     private int previousOrders;
     private long previousUnitsSold;
}
