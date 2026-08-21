package com.miniProject.AeroScale.order.service.impl;

import com.miniProject.AeroScale.BuyerModule.DTO.Response.AddAddressResponse;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.CartResponse;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import com.miniProject.AeroScale.BuyerModule.Service.CartService;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    // Strict Microservice Contracts (Only consuming other Services)
    private final CartService cartService;
    private final BuyerService buyerService;
    private final ProductService productService;

    @Override
    @Transactional
    public OrderResponse createOrder(UUID buyerId, CheckoutRequest request) {

        // 1. Fetch Cart
        List<CartResponse> cartItems = cartService.getAllCarts(buyerId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new EmptyCartException("Cannot place an order with an empty cart");
        }

        // 2. Fetch Validated Address Data via Buyer Service
        AddAddressResponse addressResponse = buyerService.getBuyerAddressForCheckout(buyerId, request.shippingAddressId());

        // 3. Create Address Snapshot
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

        // 4. Initialize Order
        Orders order = Orders.builder()
                .buyerId(buyerId)
                .shippingAddressSnapshot(snapshotAddress)
                .status(Orders.OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        // 5. Process Items & Deduct Stock via Product Service
        for (CartResponse cartItem : cartItems) {

            // This cleanly handles the lock, deduction, and returns the live price
            ProductResponse productResponse = productService.reserveStockForCheckout(
                    cartItem.getProductId(),
                    cartItem.getItemCount()
            );

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

        // 6. Save the Order
        Orders savedOrder = orderRepository.save(order);

        // 7. Clear the Cart
        cartService.clearCart(buyerId);

        return OrderResponse.fromEntity(savedOrder);
    }
}