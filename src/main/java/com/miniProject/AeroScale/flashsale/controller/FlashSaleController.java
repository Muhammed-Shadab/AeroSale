package com.miniProject.AeroScale.flashsale.controller;

import com.miniProject.AeroScale.flashsale.dto.request.ScheduleFlashSaleRequest;
import com.miniProject.AeroScale.flashsale.service.FlashSaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flash-sales")
@RequiredArgsConstructor
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    @PostMapping
    public ResponseEntity<Void> scheduleSale(
            @Valid @RequestBody ScheduleFlashSaleRequest request,
            @AuthenticationPrincipal(expression = "id") UUID sellerId) {

        flashSaleService.scheduleFlashSale(sellerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}