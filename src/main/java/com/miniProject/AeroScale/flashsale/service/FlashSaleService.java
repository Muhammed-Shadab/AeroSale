package com.miniProject.AeroScale.flashsale.service;

import com.miniProject.AeroScale.flashsale.dto.request.ScheduleFlashSaleRequest;
import com.miniProject.AeroScale.flashsale.entity.FlashSale;

import java.util.UUID;

public interface FlashSaleService {

    // order mod will use this to check if allowed ahead
    void validateFlashSalePurchase(UUID productId, int requestedQuantity);

    // Used by the Seller to create a new flashsale
    FlashSale scheduleFlashSale(UUID sellerId, ScheduleFlashSaleRequest request);
}