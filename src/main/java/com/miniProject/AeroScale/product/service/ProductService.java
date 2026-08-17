package com.miniProject.AeroScale.product.service;

import com.miniProject.AeroScale.product.dto.ProductRequest;
import com.miniProject.AeroScale.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(UUID sellerId , ProductRequest request);

    ProductResponse getProductByIdAndSellerId(UUID productId, UUID sellerId);

    Page<ProductResponse> getAllProductsBySellerId(UUID sellerId, Pageable pageable);

    ProductResponse updateProduct(UUID productId , UUID sellerId , ProductRequest request);

    void deleteProduct(UUID productId ,  UUID sellerId);



}
