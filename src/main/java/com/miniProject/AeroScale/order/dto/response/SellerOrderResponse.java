package com.miniProject.AeroScale.order.dto.response;

import com.miniProject.AeroScale.order.entity.OrderAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerOrderResponse {
        private String orderId;
        private String status;
        private OrderAddress shippingAddress;
        private ProductDetails item;
        private BigDecimal sellerTotal;
}


