package com.miniProject.AeroScale.product.dto.response;

import com.miniProject.AeroScale.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


public record ProductResponse(
        UUID id,
        UUID sellerId,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Product.ProductStatus status,
        Instant createdAt,
        Instant updatedAt,
        int totalStock,
        int availableStock
) {
    public static ProductResponse fromEntity(Product product, int totalStock, int availableStock) {
        return new ProductResponse(
                product.getId(),
                product.getSellerId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                totalStock,
                availableStock
        );
    }
}
