package com.miniProject.AeroScale.SellerModule.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalyticsResponse {
    private int rangeDays;
    private LocalDate from;
    private LocalDate to;

    private AnalyticsSummary summary;

    private List<DailyAnalytics> daily;

    private List<StatusBreakdown> statusBreakdown;

    private List<TopProductsAnalytics> topProducts;

    private RevenueBySource revenueBySource;


}
