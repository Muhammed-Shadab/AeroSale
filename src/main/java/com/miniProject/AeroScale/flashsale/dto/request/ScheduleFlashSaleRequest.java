package com.miniProject.AeroScale.flashsale.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ScheduleFlashSaleRequest(
        @NotNull(message = "Product ID is required")
        UUID productId,

        @NotNull(message = "Start time is required")
        @Future(message = "Start time must be in the future")
        Instant startTime,

        @NotNull(message = "End time is required")
        @Future(message = "End time must be in the future")
        Instant endTime,

        @NotNull(message = "Max items per user limit is required")
        @Min(value = 1, message = "Max items per user must be at least 1")
        Integer maxItemsPerUser,

        @NotNull(message = "Sale price is required")
        @DecimalMin(value = "0.01", message = "Sale price must be at least 0.01")
        BigDecimal salePrice,

        @NotNull(message = "Total quantity is required")
        @Min(value = 1, message = "Total quantity must be at least 1")
        Integer totalQuantity
) {}