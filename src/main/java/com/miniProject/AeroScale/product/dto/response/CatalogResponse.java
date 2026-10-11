package com.miniProject.AeroScale.product.dto.response;

import com.miniProject.AeroScale.flashsale.dto.response.FlashSaleInfo;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.entity.Product.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record CatalogResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        String imageUrl,
        Integer availableStock,
        FlashSaleInfo flashSale
) {
    public static CatalogResponse fromEntity(Product p, Integer availableStock, FlashSaleInfo flashSale) {
        return new CatalogResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStatus(),
                p.getImageUrl(),
                availableStock,
                flashSale
        );
    }
}