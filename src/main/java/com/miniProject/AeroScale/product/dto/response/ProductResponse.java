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
        Integer stockQuantity,
        Product.ProductStatus status,
        Long version,
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
                product.getStockQuantity(),
                product.getStatus(),
                product.getVersion(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
