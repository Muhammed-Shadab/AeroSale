package com.miniProject.AeroScale.product.service;

import com.miniProject.AeroScale.product.dto.response.CatalogResponse;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.entity.Product.ProductStatus;
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
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CatalogResponse> getActiveProducts(Pageable pageable) {
        // rules : We only fetch products where status is ACTIVE
        return productRepository.findByStatus(ProductStatus.ACTIVE, pageable)
                .map(CatalogResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogResponse getActiveProductById(UUID productId) {
        // This is for Fetch the product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // IMP : Crucial business rule: Buyers cannot view DRAFT or ARCHIVED products, even if they know the ID.
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new ProductNotFoundException("Product not found");
        }

        return CatalogResponse.fromEntity(product);
    }
}