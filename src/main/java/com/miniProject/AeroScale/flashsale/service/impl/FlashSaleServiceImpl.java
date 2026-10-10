package com.miniProject.AeroScale.flashsale.service.impl;

import com.miniProject.AeroScale.flashsale.dto.request.ScheduleFlashSaleRequest;
import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import com.miniProject.AeroScale.flashsale.repository.FlashSaleRepository;
import com.miniProject.AeroScale.flashsale.service.FlashSaleService;
import com.miniProject.AeroScale.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlashSaleServiceImpl implements FlashSaleService {

    private final FlashSaleRepository flashSaleRepository;
    private final ProductService productService; // Injected to verify the seller prod own krta hai ki nahi

    @Override
    @Transactional(readOnly = true)
    public void validateFlashSalePurchase(UUID productId, int requestedQuantity) {
        Optional<FlashSale> optionalSale = flashSaleRepository.findByProductId(productId);

        // If it's not a flash sale item, normally buy hoga ye
        if (optionalSale.isEmpty()) {
            return;
        }

        FlashSale sale = optionalSale.get();
        Instant now = Instant.now();

        if (sale.getStatus() != FlashSale.FlashSaleStatus.ACTIVE) {
            throw new IllegalStateException("This flash sale is not currently active.");
        }

        if (now.isBefore(sale.getStartTime())) {
            throw new IllegalStateException("The flash sale for this item has not started yet.");
        }

        if (now.isAfter(sale.getEndTime())) {
            throw new IllegalStateException("The flash sale for this item has ended.");
        }

        if (requestedQuantity > sale.getMaxItemsPerUser()) {
            throw new IllegalArgumentException("You can only purchase a maximum of "
                    + sale.getMaxItemsPerUser() + " units during this flash sale.");
        }
    }

    @Override
    @Transactional
    public FlashSale scheduleFlashSale(UUID sellerId, ScheduleFlashSaleRequest request) {

        // time aage hona chahiye
        if (request.startTime().isAfter(request.endTime()) || request.startTime().equals(request.endTime())) {
            throw new IllegalArgumentException("Start time must be strictly before end time");
        }

        //Cross-Module Authentication......seller own or not
        // If they don't, this productService method throws an exception and stops execution.
        productService.getProductByIdAndSellerId(request.productId(), sellerId);

        // duplicate active sale for the same  product
        if (flashSaleRepository.existsByProductId(request.productId())) {
            throw new IllegalStateException("A flash sale is already scheduled or active for this product");
        }




        //saveeeee
        FlashSale flashSale = FlashSale.builder()
                .productId(request.productId())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .maxItemsPerUser(request.maxItemsPerUser())
                .status(FlashSale.FlashSaleStatus.SCHEDULED)
                .build();

        return flashSaleRepository.save(flashSale);
    }
}