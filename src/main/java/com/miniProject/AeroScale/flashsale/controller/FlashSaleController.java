package com.miniProject.AeroScale.flashsale.controller;

import com.miniProject.AeroScale.flashsale.dto.request.ScheduleFlashSaleRequest;
import com.miniProject.AeroScale.flashsale.dto.response.SellerFlashSaleResponse;
import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import com.miniProject.AeroScale.flashsale.service.FlashSaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelSale(
            @PathVariable UUID id,
            @AuthenticationPrincipal(expression = "id") UUID sellerId) {

        flashSaleService.cancelFlashSale(sellerId, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<SellerFlashSaleResponse>> getSellerSales(@AuthenticationPrincipal(expression = "id") UUID sellerId,
                                                                        @RequestParam(required = false) FlashSale.FlashSaleStatus status,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "8") int size) {
        Page<SellerFlashSaleResponse> response = flashSaleService.getSellerSales(sellerId, status, page, size);

        return ResponseEntity.ok(response);
    }

}