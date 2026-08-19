package com.miniProject.AeroScale.product.dto.response;

import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.entity.Product.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record CatalogResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        ProductStatus status
) {
    public static CatalogResponse fromEntity(Product product) {
        return new CatalogResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus()
        );
    }
}