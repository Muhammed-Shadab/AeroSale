package com.miniProject.AeroScale.flashsale.controller;

import com.miniProject.AeroScale.flashsale.dto.request.SaleState;
import com.miniProject.AeroScale.product.dto.response.CatalogResponse;
import com.miniProject.AeroScale.product.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog/flash-sales")
@RequiredArgsConstructor
public class FlashSaleCatalogController {

    private final CatalogService catalogService;

    @GetMapping
    public ResponseEntity<Page<CatalogResponse>> list(
            @RequestParam(defaultValue = "LIVE") SaleState state,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        // sort state se fix hai, isliye Pageable nahi le rahe. size ko service 1..50 mein clamp karti hai
        return ResponseEntity.ok(catalogService.getFlashSaleCatalog(state, page, size));
    }
}