package com.miniProject.AeroScale.inventory.service.impl;

import com.miniProject.AeroScale.inventory.entity.Inventory;
import com.miniProject.AeroScale.inventory.entity.InventoryReservation;
import com.miniProject.AeroScale.inventory.repository.InventoryRepository;
import com.miniProject.AeroScale.inventory.repository.InventoryReservationRepository;
import com.miniProject.AeroScale.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryReservationRepository reservationRepository;

    // Hardcoded for now...later tp application.properties
    private static final int LOCK_TIMEOUT_MINUTES = 5;

    @Override
    @Transactional
    public InventoryReservation reserveStock(UUID productId, UUID orderId, int quantity) {

        // 1. Fetch the main ledger. This entity has @Version, so Hibernate handles concurrent locks!
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalStateException("Inventory record not found for product"));

        // 2. Check if we actually have enough buyable stock (total - locked)
        if (inventory.getAvailableStock() < quantity) {
            throw new IllegalStateException("Insufficient available stock");
        }

        // 3. Increase the locked stock
        inventory.setLockedStock(inventory.getLockedStock() + quantity);
        // (Hibernate dirty checking will update the DB and increment the @Version when transaction commits)

        // 4. Create the 5-minute tracker
        InventoryReservation reservation = InventoryReservation.builder()
                .productId(productId)
                .orderId(orderId)
                .quantityLocked(quantity)
                .status(InventoryReservation.ReservationStatus.PENDING)
                .expiresAt(Instant.now().plus(LOCK_TIMEOUT_MINUTES, ChronoUnit.MINUTES))
                .build();

        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void confirmStock(UUID orderId, UUID productId) {
        InventoryReservation reservation = getPendingReservationOrThrow(orderId, productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalStateException("Inventory record not found"));

        // Payment succeeded! We permanently deduct from BOTH total and locked.
        // Example: Total 10, Locked 4. Confirm -> Total becomes 6, Locked becomes 0. Available is still 6.
        inventory.setTotalStock(inventory.getTotalStock() - reservation.getQuantityLocked());
        inventory.setLockedStock(inventory.getLockedStock() - reservation.getQuantityLocked());

        reservation.setStatus(InventoryReservation.ReservationStatus.CONFIRMED);
    }

    @Override
    @Transactional
    public void releaseStock(UUID orderId, UUID productId) {
        InventoryReservation reservation = getPendingReservationOrThrow(orderId, productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalStateException("Inventory record not found"));

        // Buyer cancelled. Just remove the lock. Total stays the same.
        inventory.setLockedStock(inventory.getLockedStock() - reservation.getQuantityLocked());
        reservation.setStatus(InventoryReservation.ReservationStatus.CANCELLED);
    }

    @Override
    @Transactional
    public void releaseExpiredReservations() {
        // Find everything that is PENDING but the timer has run out
        List<InventoryReservation> expiredLocks = reservationRepository
                .findByStatusAndExpiresAtBefore(InventoryReservation.ReservationStatus.PENDING, Instant.now());

        if (expiredLocks.isEmpty()) {
            return;
        }

        log.info("Found {} expired stock reservations. Releasing locks...", expiredLocks.size());

        for (InventoryReservation reservation : expiredLocks) {
            Inventory inventory = inventoryRepository.findByProductId(reservation.getProductId())
                    .orElseThrow(() -> new IllegalStateException("Inventory record not found"));

            // Release the lock back to the available pool
            inventory.setLockedStock(inventory.getLockedStock() - reservation.getQuantityLocked());
            reservation.setStatus(InventoryReservation.ReservationStatus.EXPIRED);
        }
    }

    // Helper method
    private InventoryReservation getPendingReservationOrThrow(UUID orderId, UUID productId) {
        InventoryReservation reservation = reservationRepository.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new IllegalStateException("No active reservation found for this order"));

        if (reservation.getStatus() != InventoryReservation.ReservationStatus.PENDING) {
            throw new IllegalStateException("Reservation is not in PENDING state");
        }
        return reservation;
    }
}