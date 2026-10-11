package com.miniProject.AeroScale.flashsale.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FlashSaleInfo(
        UUID id,
        String state,            // "LIVE" or ya fir "UPCOMING"
        Instant startTime,
        Instant endTime,
        Integer maxItemsPerUser,
        BigDecimal salePrice,
        Integer discountPercent,
        Integer totalQuantity,
        Integer soldQuantity
) {}