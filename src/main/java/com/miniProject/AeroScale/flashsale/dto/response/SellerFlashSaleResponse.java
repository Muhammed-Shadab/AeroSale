package com.miniProject.AeroScale.flashsale.dto.response;

import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import com.miniProject.AeroScale.flashsale.entity.FlashSale.FlashSaleStatus;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

public record SellerFlashSaleResponse(
        UUID id,
        UUID productId,
        String productName,
        String imageUrl,
        BigDecimal productPrice,
        BigDecimal salePrice,
        Integer discountPercent,
        Instant startTime,
        Instant endTime,
        Integer maxItemsPerUser,
        Integer totalQuantity,
        Integer soldQuantity,
        FlashSaleStatus status
) {
    public static SellerFlashSaleResponse from(
            FlashSale sale,
            ProductResponse product) {

        BigDecimal productPrice = product.price();
        int discountPercent = 0;

        if (productPrice != null && productPrice.signum() > 0) {
            discountPercent = productPrice
                    .subtract(sale.getSalePrice())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(productPrice, 0, RoundingMode.HALF_UP)
                    .intValue();
        }

        return new SellerFlashSaleResponse(
                sale.getId(),
                sale.getProductId(),
                product.name(),
                product.imageUrl(),
                productPrice,
                sale.getSalePrice(),
                discountPercent,
                sale.getStartTime(),
                sale.getEndTime(),
                sale.getMaxItemsPerUser(),
                sale.getTotalQuantity(),
                sale.getSoldQuantity(),
                sale.getStatus()
        );
    }
}