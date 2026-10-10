package com.miniProject.AeroScale.flashsale.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ScheduleFlashSaleRequest(
        @NotNull(message = "Product ID is requied")
        UUID productId,

        @NotNull(message = "Start time is requied")
        @Future(message = "Start time must be in the future")
        Instant startTime,

        @NotNull(message = "End time is required")
        @Future(message = "End time must be in the future")
        Instant endTime,

        @NotNull(message = "Max items per user limit is reqired")
        @Min(value = 1, message = "Max items per user must be at least 1")
        Integer maxItemsPerUser
) {}