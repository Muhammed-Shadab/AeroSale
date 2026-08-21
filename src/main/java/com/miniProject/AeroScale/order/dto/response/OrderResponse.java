package com.miniProject.AeroScale.order.dto.response;

import com.miniProject.AeroScale.order.entity.OrderAddress;
import com.miniProject.AeroScale.order.entity.Orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID buyerId,
        OrderAddress shippingAddress,
        BigDecimal totalAmount,
        String status,
        List<OrderItemResponse> items,
        Instant createdAt
) {
    public static OrderResponse fromEntity(Orders order) {

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::fromEntity)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getBuyerId(),
                order.getShippingAddressSnapshot(),
                order.getTotalAmount(),
                order.getStatus().name(),
                itemResponses,
                order.getCreatedAt()
        );

    }

}