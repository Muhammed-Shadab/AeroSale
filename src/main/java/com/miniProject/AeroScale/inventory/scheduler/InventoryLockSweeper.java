package com.miniProject.AeroScale.inventory.scheduler;

import com.miniProject.AeroScale.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryLockSweeper {

    private final InventoryService inventoryService;

    // Runs every 60 seconds (60000 milliseconds)...it will clean all the expired orders inventory
    @Scheduled(fixedRate = 60000)
    public void sweepExpiredLocks() {
        log.debug("Running scheduled sweep for expired inventory locks...");
        inventoryService.releaseExpiredReservations();
    }
}