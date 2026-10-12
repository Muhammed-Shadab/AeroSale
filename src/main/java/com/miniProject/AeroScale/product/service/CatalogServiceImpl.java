package com.miniProject.AeroScale.product.service;

import com.miniProject.AeroScale.flashsale.dto.request.SaleState;
import com.miniProject.AeroScale.flashsale.dto.response.FlashSaleInfo;
import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import com.miniProject.AeroScale.flashsale.service.FlashSaleService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final FlashSaleService flashSaleService;

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
        List<Product> products = page.getContent();

        Map<UUID, Integer> stock = stockFor(products);
        Map<UUID, FlashSale> sales = flashSaleService.findOpenSales(products.stream().map(Product::getId).toList());
        Instant now = Instant.now();

        return page.map(p -> CatalogResponse.fromEntity(p,
                stock.getOrDefault(p.getId(), 0),
                infoOrNull(sales.get(p.getId()), p, now)));
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

        // sale SCHEDULED/ACTIVE aur endTime > now ho tabhi bharta hai
        FlashSale sale = flashSaleService.findOpenSales(List.of(productId)).get(productId);
        return CatalogResponse.fromEntity(product, stock, infoOrNull(sale, product, Instant.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CatalogResponse> getFlashSaleCatalog(SaleState state, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        // LIVE: jo pehle khatam hogi wo pehle. UPCOMING: jo pehle shuru hogi wo pehle
        Sort sort = Sort.by(state == SaleState.LIVE ? "endTime" : "startTime").ascending();
        Page<FlashSale> sales = flashSaleService.findSalesPage(state, PageRequest.of(safePage, safeSize, sort));

        List<UUID> ids = sales.getContent().stream().map(FlashSale::getProductId).toList();
        Map<UUID, Product> products = productRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        Map<UUID, Integer> stock = stockFor(products.values());
        Instant now = Instant.now();

        List<CatalogResponse> content = new ArrayList<>();
        for (FlashSale sale : sales.getContent()) {
            Product p = products.get(sale.getProductId());
            // archived/draft product ka sale card nahi dikhana
            if (p == null || p.getStatus() != ProductStatus.ACTIVE) continue;
            content.add(CatalogResponse.fromEntity(p, stock.getOrDefault(p.getId(), 0),
                    FlashSaleInfo.from(sale, p.getPrice(), now)));
        }
        return new PageImpl<>(content, sales.getPageable(), sales.getTotalElements());
    }

    private FlashSaleInfo infoOrNull(FlashSale sale, Product product, Instant now) {
        return sale == null ? null : FlashSaleInfo.from(sale, product.getPrice(), now);
    }

    private Map<UUID, Integer> stockFor(Collection<Product> products) {
        List<UUID> ids = products.stream().map(Product::getId).toList();
        if (ids.isEmpty()) return Map.of();
        return inventoryRepository.findByProductIdIn(ids).stream()
                .collect(Collectors.toMap(Inventory::getProductId, Inventory::getAvailableStock));
    }
}