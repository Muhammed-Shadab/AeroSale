package com.miniProject.AeroScale.flashsale.dto.response;

import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import com.miniProject.AeroScale.flashsale.entity.FlashSale.FlashSaleStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

public record FlashSaleInfo(
        UUID id,
        String state,            // "LIVE" ya "UPCOMING"
        Instant startTime,
        Instant endTime,
        Integer maxItemsPerUser,
        BigDecimal salePrice,
        Integer discountPercent, // round((price - salePrice) / price * 100)
        Integer totalQuantity,
        Integer soldQuantity
) {
    public static FlashSaleInfo from(FlashSale sale, BigDecimal regularPrice, Instant now) {
        boolean live = sale.getStatus() == FlashSaleStatus.ACTIVE
                && !now.isBefore(sale.getStartTime())
                && now.isBefore(sale.getEndTime());

        int discount = 0;
        if (regularPrice != null && regularPrice.signum() > 0) {
            discount = regularPrice.subtract(sale.getSalePrice())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(regularPrice, 0, RoundingMode.HALF_UP)
                    .intValue();
        }

        return new FlashSaleInfo(sale.getId(), live ? "LIVE" : "UPCOMING",
                sale.getStartTime(), sale.getEndTime(), sale.getMaxItemsPerUser(),
                sale.getSalePrice(), discount, sale.getTotalQuantity(), sale.getSoldQuantity());
    }
}