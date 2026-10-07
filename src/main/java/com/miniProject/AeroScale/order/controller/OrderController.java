package com.miniProject.AeroScale.order.controller;

import com.miniProject.AeroScale.order.dto.request.CheckoutRequest;
import com.miniProject.AeroScale.order.dto.response.OrderResponse;
import com.miniProject.AeroScale.order.service.OrderService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @Valid @RequestBody CheckoutRequest request,
            @AuthenticationPrincipal(expression = "id") UUID buyerId) throws RazorpayException {

        OrderResponse response = orderService.createOrder(buyerId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal(expression = "id") UUID buyerId) {

        orderService.cancelOrder(buyerId, orderId);

        return ResponseEntity.noContent().build(); // 204 No Content is standard for DELETE/Cancel
    }

    // PLACEHOLDER: WE Call this from Postman to simulate a successful payment
    @PostMapping("/{orderId}/simulate-payment")
    public ResponseEntity<Void> simulatePayment(@PathVariable UUID orderId) {
        orderService.confirmOrderPayment(orderId);
        return ResponseEntity.ok().build();
    }
}