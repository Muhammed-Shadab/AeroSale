package com.miniProject.AeroScale.order.dto.response;

import com.miniProject.AeroScale.order.entity.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
    public static OrderItemResponse fromEntity(OrderItem item) {

        return new OrderItemResponse(
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPriceAtPurchase(),
                item.getSubTotal()
        );

    }

}