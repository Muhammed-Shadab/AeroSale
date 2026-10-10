package com.miniProject.AeroScale.flashsale.repository;

import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface FlashSaleRepository extends JpaRepository<FlashSale, UUID> {
    Optional<FlashSale> findByProductId(UUID productId);
    boolean existsByProductId(UUID productId);
}