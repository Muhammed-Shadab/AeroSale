package com.miniProject.AeroScale.order.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlashSaleProductDetails {

    @NotNull(message = "Product Should Not Be Null!!")
    private UUID productId;

    @NotNull(message = "Quantity Should Not Be Null")
    @Min(value = 1, message = "Quantity Should Be Greater Then Zero!!")
    private int quantity;

    @NotNull(message = "Shipping address ID is required to place an order")
    UUID shippingAddressId;

    @Column(nullable = false)
    @Min(value = 1, message = "Price Should be Greater then the zero")
    private BigDecimal totalPrice;

}
