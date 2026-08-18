package com.miniProject.AeroScale.product.controller;

import com.miniProject.AeroScale.product.dto.ProductRequest;
import com.miniProject.AeroScale.product.dto.ProductResponse;
import com.miniProject.AeroScale.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @AuthenticationPrincipal(expression = "id") UUID sellerId,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.createProduct(sellerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(
            @AuthenticationPrincipal(expression = "id") UUID sellerId,
            @PathVariable UUID productId) {

        ProductResponse response =
                productService.getProductByIdAndSellerId(productId, sellerId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @AuthenticationPrincipal(expression = "id") UUID sellerId,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<ProductResponse> response = productService.getAllProductsBySellerId(sellerId, pageable);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @AuthenticationPrincipal(expression = "id") UUID sellerId,
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.updateProduct(productId, sellerId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @AuthenticationPrincipal(expression = "id") UUID sellerId,
            @PathVariable UUID productId) {

        productService.deleteProduct(productId, sellerId);
        return ResponseEntity.noContent().build();
    }
}