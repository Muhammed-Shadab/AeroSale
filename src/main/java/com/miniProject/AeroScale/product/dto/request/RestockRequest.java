package com.miniProject.AeroScale.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RestockRequest(
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Must add at least 1 item")
        Integer quantity
) {}