package com.miniProject.AeroScale.product.dto.request;

import java.math.BigDecimal;

public record CatalogQuery(
        String q,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        boolean inStock,
        Type type
) {
    public enum Type { ALL, REGULAR }
}