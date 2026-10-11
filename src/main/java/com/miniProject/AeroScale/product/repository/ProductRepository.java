package com.miniProject.AeroScale.product.repository;

import com.miniProject.AeroScale.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> , JpaSpecificationExecutor<Product> {

    Optional<Product> findByIdAndSellerId(UUID id, UUID sellerId);

    Page<Product> findAllBySellerId(UUID sellerId, Pageable pageable);

    Page<Product> findByStatus(Product.ProductStatus status, Pageable pageable);

}
