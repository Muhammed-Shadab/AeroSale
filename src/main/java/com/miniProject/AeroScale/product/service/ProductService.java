package com.miniProject.AeroScale.product.service;

import com.miniProject.AeroScale.product.dto.request.ProductRequest;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;
import com.miniProject.AeroScale.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(UUID sellerId , ProductRequest request , String imageUrl);

    ProductResponse getProductByIdAndSellerId(UUID productId, UUID sellerId);

    Page<ProductResponse> getAllProductsBySellerId(UUID sellerId, Pageable pageable);

    ProductResponse updateProduct(UUID productId , UUID sellerId , ProductRequest request , String imageUrl);

    void deleteProduct(UUID productId ,  UUID sellerId);

    // Internal cross-module method for Order module
    ProductResponse getProductForCheckout(UUID productId);

    String getProductNameByProductId(UUID productId);

    String getProductImageByProductId(UUID productId);

    Product findProductByIdOrThrow(UUID productId);
}
