package com.miniProject.AeroScale.SellerModule.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.boot.internal.Abstract;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DailyAnalytics {
    private LocalDate date;
    private BigDecimal revenue;
    private int orders;
    private int unitsSold;
}
