package com.miniProject.AeroScale.SellerModule.DTO.Response;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RevenueBySource {
    private BigDecimal flashSale;
    private BigDecimal regular;
}
