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
public class ItemDataForCart {

    @NotNull(message = "Product Id should be there")
    private UUID productId;

    @NotNull(message = "Atleast one quantity of the respective item")
    @Min(value = 1, message = "Atleast one quantity of the respective item")
    private int count;
}
