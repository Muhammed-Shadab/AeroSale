package com.miniProject.AeroScale.order.service.impl;

import com.miniProject.AeroScale.BuyerModule.DTO.Response.AddAddressResponse;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.CartResponse;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import com.miniProject.AeroScale.BuyerModule.Service.CartService;
import com.miniProject.AeroScale.inventory.service.InventoryService;
import com.miniProject.AeroScale.order.dto.request.CheckoutRequest;
import com.miniProject.AeroScale.order.dto.response.OrderResponse;
import com.miniProject.AeroScale.order.entity.OrderAddress;
import com.miniProject.AeroScale.order.entity.OrderItem;
import com.miniProject.AeroScale.order.entity.Orders;
import com.miniProject.AeroScale.order.exception.EmptyCartException;
import com.miniProject.AeroScale.order.repository.OrderRepository;
import com.miniProject.AeroScale.order.service.OrderService;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;
import com.miniProject.AeroScale.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    // Strict Microservice Contracts
    private final CartService cartService;
    private final BuyerService buyerService;
    private final ProductService productService;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public OrderResponse createOrder(UUID buyerId, CheckoutRequest request) {

        // 1. IDEMPOTENCY GUARD: Check if this checkout attempt already succeeded
        Optional<Orders> existingOrder = orderRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existingOrder.isPresent()) {
            return OrderResponse.fromEntity(existingOrder.get());
        }

        // 2. Fetch Cart
        List<CartResponse> cartItems = cartService.getAllCarts(buyerId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new EmptyCartException("Cannot place an order with an empty cart");
        }

        // 3. Fetch Validated Address
        AddAddressResponse addressResponse = buyerService.getBuyerAddressForCheckout(buyerId, request.shippingAddressId());
        OrderAddress snapshotAddress = OrderAddress.builder()
                .recipientName(addressResponse.getRecipientName())
                .recipientPhoneNo(addressResponse.getRecipientPhoneNo())
                .addressLine1(addressResponse.getAddressLine1())
                .addressLine2(addressResponse.getAddressLine2())
                .city(addressResponse.getCity())
                .state(addressResponse.getState())
                .pincode(addressResponse.getPincode())
                .country(addressResponse.getCountry())
                .build();

        // 4. Initialize Order (Pre-generate UUID so Inventory can use it)
        UUID orderId = UUID.randomUUID();
        Orders order = Orders.builder()
                .id(orderId)
                .buyerId(buyerId)
                .idempotencyKey(request.idempotencyKey())
                .shippingAddressSnapshot(snapshotAddress)
                .status(Orders.OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        // 5. Process Items: Fetch Price & Reserve Stock
        for (CartResponse cartItem : cartItems) {

            // Fetch read-only price data
            ProductResponse productResponse = productService.getProductForCheckout(cartItem.getProductId());

            // Lock the stock in the Vault (Throws exception if unavailable)
            inventoryService.reserveStock(cartItem.getProductId(), orderId, cartItem.getItemCount());

            BigDecimal subTotal = productResponse.price().multiply(BigDecimal.valueOf(cartItem.getItemCount()));
            calculatedTotal = calculatedTotal.add(subTotal);

            OrderItem orderItem = OrderItem.builder()
                    .productId(productResponse.id())
                    .quantity(cartItem.getItemCount())
                    .unitPriceAtPurchase(productResponse.price())
                    .subTotal(subTotal)
                    .build();

            order.addOrderItem(orderItem);
        }

        order.setTotalAmount(calculatedTotal);

        // 6. Save the Order & Clear Cart
        Orders savedOrder = orderRepository.save(order);
        cartService.clearCart(buyerId);

        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(UUID buyerId, UUID orderId) {
        Orders order = orderRepository.findByIdAndBuyerId(orderId, buyerId)
                .orElseThrow(() -> new RuntimeException("Order not found or access denied"));

        // We only allow cancellation if payment hasn't been confirmed yet
        if (order.getStatus() != Orders.OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be cancelled");
        }

        // Loop through the items and tell the vault to release the locks
        for (OrderItem item : order.getOrderItems()) {
            inventoryService.releaseStock(orderId, item.getProductId());
        }

        // Mark as cancelled and save
        order.setStatus(Orders.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void confirmOrderPayment(UUID orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != Orders.OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be confirmed");
        }

        // Tell the Vault to permanently deduct the physical stock
        for (OrderItem item : order.getOrderItems()) {
            inventoryService.confirmStock(orderId, item.getProductId());
        }

        order.setStatus(Orders.OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }
}