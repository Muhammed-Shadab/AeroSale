package com.miniProject.AeroScale.product.dto.response;

import com.miniProject.AeroScale.product.entity.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID sellerId,
        String name,
        String description,
        BigDecimal price,
        Product.ProductStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSellerId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
