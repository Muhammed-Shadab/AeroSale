package com.miniProject.AeroScale.BuyerModule.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {

    private UUID id;
    private UUID productId;
    private int itemCount;
    private String itemName;
    private BigDecimal pricePerItem;
    private int CurrentStockOfProduct;
    private Instant createdAt;
    private Instant updatedAt;



}
