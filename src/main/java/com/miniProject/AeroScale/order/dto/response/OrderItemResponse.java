package com.miniProject.AeroScale.order.dto.response;

import com.miniProject.AeroScale.order.entity.OrderItem;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemResponse(
        UUID productId,
        String productName,
        String imageUrl,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
    public static OrderItemResponse fromEntity(OrderItem item) {

        return new OrderItemResponse(
                item.getProductId(),
                null,
                null,
                item.getQuantity(),
                item.getUnitPriceAtPurchase(),
                item.getSubTotal()
        );

    }

    public static OrderItemResponse fromEntity(OrderItem item, String productName, String image) {

        return new OrderItemResponse(
                item.getProductId(),
                productName,
                image,
                item.getQuantity(),
                item.getUnitPriceAtPurchase(),
                item.getSubTotal()
        );

    }

}