package com.miniProject.AeroScale.inventory.service;

import com.miniProject.AeroScale.inventory.entity.InventoryReservation;

import java.util.UUID;

public interface InventoryService {

    /**
     * Called by the Order Module during checkout.
     * Temporarily locks stock for 5 minutes.
     */
    InventoryReservation reserveStock(UUID productId, UUID orderId, int quantity);

    /**
     * Called by the Payment Module (future) upon successful transaction.
     * Converts a PENDING reservation to CONFIRMED and permanently deducts physical stock and locked stock.
     */
    void confirmStock(UUID orderId, UUID productId);

    /**
     * Called by a background Cron Job.
     * Sweeps the database for EXPIRED locks and returns the stock to the available pool.
     */
    void releaseExpiredReservations();

    /**
     * Called by the Order Module if a buyer explicitly clicks "Cancel Order" before paying.
     */
    void releaseStock(UUID orderId, UUID productId);
}