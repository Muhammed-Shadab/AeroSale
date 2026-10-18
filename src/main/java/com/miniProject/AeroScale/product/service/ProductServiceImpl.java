package com.miniProject.AeroScale.product.service;

import com.miniProject.AeroScale.inventory.service.InventoryService;
import com.miniProject.AeroScale.product.dto.request.ProductRequest;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.exception.ProductNotFoundException;
import com.miniProject.AeroScale.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.PageRanges;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public ProductResponse createProduct(UUID sellerId, ProductRequest request , String imageUrl) {
        Product product = Product.builder()
                .sellerId(sellerId)
                .name(request.name())
                .description((request.description()))
                .price(request.price())
                .imageUrl(imageUrl)
                .status(request.status())
                .build();
        Product savedProduct = productRepository.save(product);
        // HERE WE Open the zero-stock ledger in the Vault immediately.this will create a zero stock row in the inventory table for this particular product.
        inventoryService.initializeInventory(savedProduct.getId());
        List<List<Integer>> inventoryInfo = inventoryService.getTotalStockThatAdded(List.of(savedProduct.getId()));
        return ProductResponse.fromEntity(savedProduct, inventoryInfo.get(0).get(0), inventoryInfo.get(0).get(1));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductByIdAndSellerId(UUID productId, UUID sellerId) {
        Product product = fetchProduct(productId , sellerId);
        List<List<Integer>> inventoryInfo = inventoryService.getTotalStockThatAdded(List.of(productId));
        return ProductResponse.fromEntity(product, inventoryInfo.get(0).get(0), inventoryInfo.get(0).get(1));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProductsBySellerId(UUID sellerId, Pageable pageable,  Product.ProductStatus status, String q) {


        Page<Product> productsPage = productRepository.findAllBySellerId(sellerId, pageable, status);
        List<Product> productList = productsPage.getContent();

        List<UUID> productIdsList = new ArrayList<>();
        for(Product product: productList) productIdsList.add(product.getId());

        List<List<Integer>> inventoryInfo = inventoryService.getTotalStockThatAdded(productIdsList);
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(int i=0;i<productIdsList.size();i++) {
            productResponseList.add(ProductResponse.fromEntity(productList.get(i), inventoryInfo.get(i).get(0), inventoryInfo.get(i).get(1)));
        }

        return new PageImpl<>(productResponseList, pageable, productResponseList.size());


    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID productId, UUID sellerId, ProductRequest request , String imageUrl) {
        Product product = fetchProduct(productId, sellerId);

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStatus(request.status());

        if (imageUrl != null) product.setImageUrl(imageUrl); // agar new img ayegi toh hi updte wnra no update

        Product updatedProduct = productRepository.save(product);
        List<List<Integer>> inventoryInfo = inventoryService.getTotalStockThatAdded(List.of(productId));
        return ProductResponse.fromEntity(updatedProduct, inventoryInfo.get(0).get(0), inventoryInfo.get(0).get(1));
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

        return ProductResponse.fromEntity(product, 0, 0);
    }

    @Override
    public String getProductNameByProductId(UUID productId) {
        Product product =  productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("product Not found"));
        return product.getName();
    }

    @Override
    public String getProductImageByProductId(UUID productId) {
        Product product =  productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("product Not found"));
        return product.getImageUrl();
    }

    @Override
    public Product findProductByIdOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product is not Available!!"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByIds(
            Collection<UUID> productIds) {

        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        List<Product> productList = productRepository.findAllById(productIds);
        List<ProductResponse> res = new ArrayList<>();

        for(int i=0;i<productList.size();i++) {
            res.add(ProductResponse.fromEntity(productList.get(i), 0, 0));
        }
        return res;

    }


    // Helper method to enforce data isolation (DRY Principle)
    private Product fetchProduct(UUID productId, UUID sellerId) {
        return productRepository.findByIdAndSellerId(productId, sellerId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found or you do not have permission to access it"));
    }
}
