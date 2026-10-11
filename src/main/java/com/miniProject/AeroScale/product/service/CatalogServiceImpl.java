package com.miniProject.AeroScale.product.service;

import com.miniProject.AeroScale.inventory.entity.Inventory;
import com.miniProject.AeroScale.inventory.repository.InventoryRepository;
import com.miniProject.AeroScale.product.dto.request.CatalogQuery;
import com.miniProject.AeroScale.product.dto.response.CatalogResponse;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.entity.Product.ProductStatus;
import com.miniProject.AeroScale.product.exception.ProductNotFoundException;
import com.miniProject.AeroScale.product.repository.ProductRepository;
import com.miniProject.AeroScale.product.repository.ProductSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CatalogResponse> searchProducts(CatalogQuery query, Pageable pageable) {
        Specification<Product> spec = ProductSpecs.active();
        if (query.q() != null && !query.q().isBlank()) spec = spec.and(ProductSpecs.textContains(query.q()));
        if (query.minPrice() != null) spec = spec.and(ProductSpecs.priceAtLeast(query.minPrice()));
        if (query.maxPrice() != null) spec = spec.and(ProductSpecs.priceAtMost(query.maxPrice()));
        if (query.inStock()) spec = spec.and(ProductSpecs.inStock());
        if (query.type() == CatalogQuery.Type.REGULAR) spec = spec.and(ProductSpecs.regularOnly());

        Page<Product> page = productRepository.findAll(spec, pageable);
        Map<UUID, Integer> stock = stockFor(page.getContent());

        // flashSale abhi null, Step 2 mein bharenge
        return page.map(p -> CatalogResponse.fromEntity(p, stock.getOrDefault(p.getId(), 0), null));
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogResponse getActiveProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Buyer DRAFT/ARCHIVED product nahi dekh sakta, ID pata ho tab bhi
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new ProductNotFoundException("Product not found");
        }

        int stock = inventoryRepository.findByProductId(productId)
                .map(Inventory::getAvailableStock).orElse(0);
        return CatalogResponse.fromEntity(product, stock, null);
    }

    private Map<UUID, Integer> stockFor(List<Product> products) {
        List<UUID> ids = products.stream().map(Product::getId).toList();
        if (ids.isEmpty()) return Map.of();
        return inventoryRepository.findByProductIdIn(ids).stream()
                .collect(Collectors.toMap(Inventory::getProductId, Inventory::getAvailableStock));
    }
}