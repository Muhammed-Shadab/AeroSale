package com.miniProject.AeroScale.BuyerModule.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartItemRequest {

    @NotNull(message = "CartId Not Found")
    private UUID Cartid;

    @NotNull(message = "Quantity is Required")
    @Min(value = 1, message = "Quantity must be at least 1. Use DELETE to remove the item instead.")
    private int itemCount;
}
