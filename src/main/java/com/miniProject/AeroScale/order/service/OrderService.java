package com.miniProject.AeroScale.order.service;

import com.miniProject.AeroScale.order.dto.request.CheckoutRequest;
import com.miniProject.AeroScale.order.dto.response.OrderResponse;
import com.miniProject.AeroScale.order.dto.response.OrdersResponseOfBuyer;
import com.miniProject.AeroScale.order.dto.response.SellerOrderResponse;
import com.miniProject.AeroScale.order.entity.OrderItem;
import com.miniProject.AeroScale.order.entity.Orders;
import com.razorpay.RazorpayException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(UUID buyerId, CheckoutRequest request) throws RazorpayException;

    void cancelOrder(UUID buyerId, UUID orderId);

    // Simulates a successful payment webhook : we do not have a payment service right now, for now we just simulate.
    void confirmOrderPayment(UUID orderId, UUID id);

    OrdersResponseOfBuyer getOrderOfBuyer(UUID id, Pageable pageable,  Orders.OrderStatus status);

    List<SellerOrderResponse> getOrderRelatedToSeller(UUID id, int page, int size, OrderItem.OrderItemStatus status);

    ResponseEntity<?> updateOrderStatus(UUID id, UUID orderId);

    //ResponseEntity<?> updateOrderStatus(UUID id, UUID orderId, String status);
}