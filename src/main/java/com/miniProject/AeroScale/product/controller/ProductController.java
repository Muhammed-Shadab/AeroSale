package com.miniProject.AeroScale.product.controller;

import com.miniProject.AeroScale.inventory.service.InventoryService;
import com.miniProject.AeroScale.product.dto.request.ProductRequest;
import com.miniProject.AeroScale.product.dto.request.RestockRequest;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;
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
    private final InventoryService inventoryService;


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

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Void> restockProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody RestockRequest request,
            @AuthenticationPrincipal(expression = "id") UUID sellerId) {

        // 1. Verify the seller actually owns this product
        // If they don't, this throws a ProductNotFoundException and stops execution.
        productService.getProductByIdAndSellerId(productId, sellerId);

        // 2. Now that we trust the caller, we add the stock.
        inventoryService.addStock(productId, request.quantity());

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @AuthenticationPrincipal(expression = "id") UUID sellerId,
            @PathVariable UUID productId) {

        productService.deleteProduct(productId, sellerId);
        return ResponseEntity.noContent().build();
    }
}