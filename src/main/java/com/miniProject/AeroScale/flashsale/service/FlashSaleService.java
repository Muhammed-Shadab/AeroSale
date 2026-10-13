package com.miniProject.AeroScale.flashsale.service;

import com.miniProject.AeroScale.flashsale.dto.request.SaleState;
import com.miniProject.AeroScale.flashsale.dto.request.ScheduleFlashSaleRequest;
import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface FlashSaleService {

    // order module checkout se pehle check karta hai
    void validateFlashSalePurchase(UUID productId, int requestedQuantity);

    // seller naya flash sale banata hai
    FlashSale scheduleFlashSale(UUID sellerId, ScheduleFlashSaleRequest request);

    // abhi LIVE sale (ACTIVE aur start <= now < end). Cart aur checkout price ke liye
    Optional<FlashSale> findLiveSale(UUID productId);

    // SCHEDULED/ACTIVE aur endTime > now wali sales, productId -> sale. Catalog ke liye
    Map<UUID, FlashSale> findOpenSales(Collection<UUID> productIds);

    // scheduler isse status badalta hai
    void refreshStatuses();

    // GET /catalog/flash-sales ke liye: LIVE ya UPCOMING sales ka ek page
    Page<FlashSale> findSalesPage(SaleState state, Pageable pageable);

    // payment confirm hone par: us product ki ACTIVE sale ka soldQuantity += quantity
    void recordSale(UUID productId, int quantity);
}