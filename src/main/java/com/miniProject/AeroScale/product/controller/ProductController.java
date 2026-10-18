package com.miniProject.AeroScale.product.controller;

import com.miniProject.AeroScale.inventory.service.InventoryService;
import com.miniProject.AeroScale.product.dto.request.ProductRequest;
import com.miniProject.AeroScale.product.dto.request.RestockRequest;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.service.ImageUploadService;
import com.miniProject.AeroScale.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class ProductController {

    private final ProductService productService;
    private final InventoryService inventoryService;

    private static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;

    private final ImageUploadService imageUploadService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> createProduct(
            @AuthenticationPrincipal(expression = "id") UUID sellerId,
            @Valid @RequestPart("product") ProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        String imageUrl = uploadIfPresent(image);
        ProductResponse response = productService.createProduct(sellerId, request, imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/test")
    public String test() {
        return "working";
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
            @AuthenticationPrincipal(expression = "id") UUID sellerId
            , @PageableDefault(
            size = 8,
            sort = "createdAt",
            direction = Sort.Direction.DESC
            ) Pageable pageable
            , @RequestParam(value = "status", required = false) Product.ProductStatus status
            ,@RequestParam(value = "q", required = false) String q) {

        Page<ProductResponse> response = productService.getAllProductsBySellerId(sellerId, pageable, status, q);

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(
            @AuthenticationPrincipal(expression = "id") UUID sellerId,
            @PathVariable UUID productId,
            @Valid @RequestPart("product") ProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        String imageUrl = uploadIfPresent(image);
        ProductResponse response = productService.updateProduct(productId, sellerId, request, imageUrl);
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

    /** Image nahi aayi to null. Galat type ya bahut badi file par 400 (IllegalArgumentException handler se). */
    private String uploadIfPresent(MultipartFile image) {
        if (image == null || image.isEmpty()) return null;
        String type = image.getContentType();
        if (type == null || !type.startsWith("image/"))
            throw new IllegalArgumentException("Only image files are allowed");
        if (image.getSize() > MAX_IMAGE_BYTES)
            throw new IllegalArgumentException("Image must be 5 MB or smaller");
        return imageUploadService.saveImage(image);
    }

    public String getProductNameByProductId(UUID productId) {
        return productService.getProductNameByProductId(productId);
    }

}