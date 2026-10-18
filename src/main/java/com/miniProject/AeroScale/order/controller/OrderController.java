package com.miniProject.AeroScale.order.controller;

import com.miniProject.AeroScale.order.dto.request.CheckoutRequest;
import com.miniProject.AeroScale.order.dto.response.OrderResponse;
import com.miniProject.AeroScale.order.dto.response.OrdersResponseOfBuyer;
import com.miniProject.AeroScale.order.dto.response.SellerOrderResponse;
import com.miniProject.AeroScale.order.entity.OrderItem;
import com.miniProject.AeroScale.order.entity.Orders;
import com.miniProject.AeroScale.order.service.OrderService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public ResponseEntity<OrdersResponseOfBuyer> getOrdersOfBuyer(
                              @AuthenticationPrincipal(expression = "id") UUID id
                            , @RequestParam(defaultValue = "0") int page
                            , @RequestParam(defaultValue = "8") int size
                            , @RequestParam(defaultValue = "status") String status) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.getOrderOfBuyer(id, page, size, status));
    }

    @GetMapping("/getOrder")
    public List<SellerOrderResponse> getOrdersRelatedToSeller(@AuthenticationPrincipal(expression = "id") UUID id,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "8") int size,
                                                              @RequestParam(required = false) Orders.OrderStatus status) {
        return orderService.getOrderRelatedToSeller(id, page, size, status);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateTheOrderStatus(@AuthenticationPrincipal(expression = "id") UUID id
                                                  ,@PathVariable(name = "orderId") UUID orderId) {
        return orderService.updateOrderStatus(id, orderId);
    }


}