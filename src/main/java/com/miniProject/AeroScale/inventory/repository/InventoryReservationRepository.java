package com.miniProject.AeroScale.inventory.repository;

import com.miniProject.AeroScale.inventory.entity.InventoryReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, UUID> {

    // Used by our cron job to find locks that timed out
    List<InventoryReservation> findByStatusAndExpiresAtBefore(
            InventoryReservation.ReservationStatus status, Instant time);

    // Used for our idempotency check
    Optional<InventoryReservation> findByOrderIdAndProductId(UUID orderId, UUID productId);
}