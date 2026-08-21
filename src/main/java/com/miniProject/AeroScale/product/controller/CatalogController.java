package com.miniProject.AeroScale.product.controller;

import com.miniProject.AeroScale.product.dto.response.CatalogResponse;
import com.miniProject.AeroScale.product.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog/products")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping
    public ResponseEntity<Page<CatalogResponse>> getActiveProducts(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<CatalogResponse> response = catalogService.getActiveProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CatalogResponse> getProductById(@PathVariable UUID productId) {

        CatalogResponse response = catalogService.getActiveProductById(productId);
        return ResponseEntity.ok(response);
    }
}