package com.miniProject.AeroScale.flashsale.scheduler;

import com.miniProject.AeroScale.flashsale.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlashSaleStatusScheduler {

    private final FlashSaleService flashSaleService;

    // SCHEDULED -> ACTIVE aur ACTIVE/SCHEDULED -> ENDED, har 30 second
    @Scheduled(fixedRate = 30000)
    public void refreshStatuses() {
        flashSaleService.refreshStatuses();
    }
}