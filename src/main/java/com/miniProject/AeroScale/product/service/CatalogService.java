package com.miniProject.AeroScale.product.service;

import com.miniProject.AeroScale.product.dto.request.CatalogQuery;
import com.miniProject.AeroScale.product.dto.response.CatalogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CatalogService {

    Page<CatalogResponse> searchProducts(CatalogQuery query, Pageable pageable);

    CatalogResponse getActiveProductById(UUID productId);
}