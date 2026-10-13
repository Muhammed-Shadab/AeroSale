package com.miniProject.AeroScale.flashsale.service.impl;

import com.miniProject.AeroScale.flashsale.dto.request.SaleState;
import com.miniProject.AeroScale.flashsale.dto.request.ScheduleFlashSaleRequest;
import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import com.miniProject.AeroScale.flashsale.entity.FlashSale.FlashSaleStatus;
import com.miniProject.AeroScale.flashsale.repository.FlashSaleRepository;
import com.miniProject.AeroScale.flashsale.service.FlashSaleService;
import com.miniProject.AeroScale.inventory.service.InventoryService;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;
import com.miniProject.AeroScale.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashSaleServiceImpl implements FlashSaleService {

    // "khuli" sale = abhi tak khatam ya cancel nahi hui
    private static final List<FlashSaleStatus> OPEN = List.of(FlashSaleStatus.SCHEDULED, FlashSaleStatus.ACTIVE);

    private final FlashSaleRepository flashSaleRepository;
    private final ProductService productService;
    private final InventoryService inventoryService;

    @Override
    @Transactional(readOnly = true)
    public void validateFlashSalePurchase(UUID productId, int requestedQuantity) {
        Optional<FlashSale> optionalSale = flashSaleRepository.findFirstByProductIdAndStatusIn(productId, OPEN);

        // sale nahi hai (ya ENDED/CANCELLED ho chuki) to product normal bikta hai
        if (optionalSale.isEmpty()) return;

        FlashSale sale = optionalSale.get();
        Instant now = Instant.now();

        if (now.isBefore(sale.getStartTime())) {
            throw new IllegalStateException("The flash sale for this item has not started yet.");
        }
        // time nikal chuka, scheduler abhi status nahi badal paya: sale khatam, normal purchase
        if (!now.isBefore(sale.getEndTime())) return;

        if (sale.getStatus() != FlashSaleStatus.ACTIVE) {
            throw new IllegalStateException("This flash sale is not currently active.");
        }
        if (requestedQuantity > sale.getMaxItemsPerUser()) {
            throw new IllegalArgumentException("You can only purchase a maximum of "
                    + sale.getMaxItemsPerUser() + " units during this flash sale.");
        }

        int remaining = sale.getTotalQuantity() - sale.getSoldQuantity();
        if (requestedQuantity > remaining) {
            throw new IllegalStateException(remaining <= 0
                    ? "This flash sale is sold out."
                    : "Only " + remaining + " units are left in this flash sale.");
        }
    }

    @Override
    @Transactional
    public FlashSale scheduleFlashSale(UUID sellerId, ScheduleFlashSaleRequest request) {

        if (!request.startTime().isBefore(request.endTime())) {
            throw new IllegalArgumentException("Start time must be strictly before end time");
        }

        // seller is product ka malik hai ya nahi (nahi to ye exception throw karta hai)
        ProductResponse product = productService.getProductByIdAndSellerId(request.productId(), sellerId);

        if (request.salePrice().compareTo(product.price()) >= 0) {
            throw new IllegalArgumentException("Sale price must be lower than the regular price");
        }
        if (request.totalQuantity() > inventoryService.getStock(request.productId())) {
            throw new IllegalArgumentException("Sale quantity cannot exceed the available stock");
        }

        // ek product par ek waqt mein ek hi khuli sale
        if (flashSaleRepository.existsByProductIdAndStatusIn(request.productId(), OPEN)) {
            throw new IllegalStateException("A flash sale is already scheduled or active for this product");
        }

        FlashSale flashSale = FlashSale.builder()
                .productId(request.productId())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .maxItemsPerUser(request.maxItemsPerUser())
                .salePrice(request.salePrice())
                .totalQuantity(request.totalQuantity())
                .soldQuantity(0)
                .status(FlashSaleStatus.SCHEDULED)
                .build();

        return flashSaleRepository.save(flashSale);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlashSale> findLiveSale(UUID productId) {
        Instant now = Instant.now();
        return flashSaleRepository.findFirstByProductIdAndStatusIn(productId, OPEN)
                .filter(s -> s.getStatus() == FlashSaleStatus.ACTIVE
                        && !now.isBefore(s.getStartTime())
                        && now.isBefore(s.getEndTime()));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, FlashSale> findOpenSales(Collection<UUID> productIds) {
        if (productIds.isEmpty()) return Map.of();
        return flashSaleRepository
                .findByProductIdInAndStatusInAndEndTimeAfter(productIds, OPEN, Instant.now())
                .stream()
                .collect(Collectors.toMap(FlashSale::getProductId, Function.identity(), (a, b) -> a));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlashSale> findSalesPage(SaleState state, Pageable pageable) {
        Instant now = Instant.now();
        if (state == SaleState.LIVE) {
            // ACTIVE aur start <= now < end
            return flashSaleRepository.findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(
                    FlashSaleStatus.ACTIVE, now, now, pageable);
        }
        // SCHEDULED aur start > now
        return flashSaleRepository.findByStatusAndStartTimeGreaterThan(
                FlashSaleStatus.SCHEDULED, now, pageable);
    }

    @Override
    @Transactional
    public void recordSale(UUID productId, int quantity) {
        flashSaleRepository.addSold(productId, quantity, FlashSaleStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void refreshStatuses() {
        Instant now = Instant.now();
        // pehle jo khatam ho chuki (SCHEDULED/ACTIVE dono), phir jo shuru honi chahiye
        int ended = flashSaleRepository.markEnded(FlashSaleStatus.ENDED, OPEN, now);
        int started = flashSaleRepository.markActive(FlashSaleStatus.ACTIVE, FlashSaleStatus.SCHEDULED, now);
        if (ended > 0 || started > 0) {
            log.info("Flash sale status refresh: {} started, {} ended", started, ended);
        }
    }
}