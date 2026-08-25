package com.miniProject.AeroScale.order.service;

import com.miniProject.AeroScale.order.dto.request.CheckoutRequest;
import com.miniProject.AeroScale.order.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(UUID buyerId, CheckoutRequest request);

    void cancelOrder(UUID buyerId, UUID orderId);

    // Simulates a successful payment webhook : we do not have a payment service right now, for now we just simulate.
    void confirmOrderPayment(UUID orderId);
}