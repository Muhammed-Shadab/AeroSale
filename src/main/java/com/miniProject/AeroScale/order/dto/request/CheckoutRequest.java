package com.miniProject.AeroScale.order.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CheckoutRequest(

        @NotNull(message = "Shipping address ID is required to place an order")
        UUID shippingAddressId

) {}