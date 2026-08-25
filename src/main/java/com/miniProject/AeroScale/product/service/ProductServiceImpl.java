package com.miniProject.AeroScale.product.service;

import com.miniProject.AeroScale.inventory.service.InventoryService;
import com.miniProject.AeroScale.product.dto.request.ProductRequest;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.exception.ProductNotFoundException;
import com.miniProject.AeroScale.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public ProductResponse createProduct(UUID sellerId, ProductRequest request) {
        Product product = Product.builder()
                .sellerId(sellerId)
                .name(request.name())
                .description((request.description()))
                .price(request.price())
                .status(request.status())
                .build();
        Product savedProduct = productRepository.save(product);
        // HERE WE Open the zero-stock ledger in the Vault immediately.this will create a zero stock row in the inventory table for this particular product.
        inventoryService.initializeInventory(savedProduct.getId());

        return ProductResponse.fromEntity(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductByIdAndSellerId(UUID productId, UUID sellerId) {
        Product product = fetchProduct(productId , sellerId);
        return ProductResponse.fromEntity(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProductsBySellerId(UUID sellerId, Pageable pageable) {
        return productRepository.findAllBySellerId(sellerId, pageable)
                .map(ProductResponse::fromEntity); // Spring Data maps the content of the Page
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID productId, UUID sellerId, ProductRequest request) {
        Product product = fetchProduct(productId, sellerId);

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStatus(request.status());

        Product updatedProduct = productRepository.save(product);
        return ProductResponse.fromEntity(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId, UUID sellerId) {
        Product product = fetchProduct(productId, sellerId);

        // IMP : DO NOT DO HARD DELETE.WE DO Soft delete: Change status to ARCHIVED.
        product.setStatus(Product.ProductStatus.ARCHIVED);

        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductForCheckout(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getStatus() != Product.ProductStatus.ACTIVE) {
            throw new IllegalStateException("Product is no longer available: " + product.getName());
        }

        return ProductResponse.fromEntity(product);
    }

    // Helper method to enforce data isolation (DRY Principle)
    private Product fetchProduct(UUID productId, UUID sellerId) {
        return productRepository.findByIdAndSellerId(productId, sellerId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found or you do not have permission to access it"));
    }
}
