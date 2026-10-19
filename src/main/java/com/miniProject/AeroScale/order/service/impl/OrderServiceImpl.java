package com.miniProject.AeroScale.order.service.impl;

import com.miniProject.AeroScale.BuyerModule.DTO.Response.AddAddressResponse;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.CartResponse;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import com.miniProject.AeroScale.BuyerModule.Service.CartService;
import com.miniProject.AeroScale.Payment.DTO.Response.RazorPayOrderResponse;
import com.miniProject.AeroScale.Payment.Service.PaymentService;
import com.miniProject.AeroScale.SellerModule.Service.SellerService;
import com.miniProject.AeroScale.flashsale.entity.FlashSale;
import com.miniProject.AeroScale.flashsale.service.FlashSaleService;
import com.miniProject.AeroScale.inventory.service.InventoryService;
import com.miniProject.AeroScale.order.dto.request.CheckoutRequest;

import com.miniProject.AeroScale.order.dto.response.*;
import com.miniProject.AeroScale.order.entity.OrderAddress;
import com.miniProject.AeroScale.order.entity.OrderItem;
import com.miniProject.AeroScale.order.entity.Orders;
import com.miniProject.AeroScale.order.exception.EmptyCartException;
import com.miniProject.AeroScale.order.exception.OrderNotAvailabe;
import com.miniProject.AeroScale.order.repository.OrderItemRepository;
import com.miniProject.AeroScale.order.repository.OrderRepository;
import com.miniProject.AeroScale.order.service.OrderService;
import com.miniProject.AeroScale.product.dto.response.ProductResponse;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.service.ProductService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value("${razorpay-key}")
    private String key;

    private final OrderRepository orderRepository;
    private final FlashSaleService flashSaleService;
    private final OrderItemRepository orderItemRepository;

    // Strict Microservice Contracts
    private final CartService cartService;
    private final BuyerService buyerService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(UUID buyerId, CheckoutRequest request) throws RazorpayException {

        // 1. IDEMPOTENCY GUARD: Check if this checkout attempt already succeeded
        Optional<Orders> existingOrder = orderRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existingOrder.isPresent()) {
            return OrderResponse.fromEntity(existingOrder.get(), paymentService.getRazorPayOrderId(existingOrder.get().getId()), key, null);
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

            flashSaleService.validateFlashSalePurchase(cartItem.getProductId(), cartItem.getItemCount());

            // price backend decide karta hai: sale LIVE ho to salePrice, warna regular price
            BigDecimal unitPrice = productResponse.price();
            Optional<FlashSale> sale = flashSaleService.findLiveSale(cartItem.getProductId());

            boolean isSalePresent = false;
            if(sale.isPresent()) {
                unitPrice = sale.get().getSalePrice();
                isSalePresent = true;
            }

            // Lock the stock in the Vault (Throws exception if unavailable)
            inventoryService.reserveStock(cartItem.getProductId(), orderId, cartItem.getItemCount());

            BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getItemCount()));
            calculatedTotal = calculatedTotal.add(subTotal);

            OrderItem orderItem = OrderItem.builder()
                    .productId(productResponse.id())
                    .quantity(cartItem.getItemCount())
                    .unitPriceAtPurchase(unitPrice)
                    .subTotal(subTotal)
                    .isFlashSaleProduct(isSalePresent)
                    .build();

            order.addOrderItem(orderItem);
        }

        order.setTotalAmount(calculatedTotal);

        // 6. Save the Order & Clear Cart
        Orders savedOrder = orderRepository.save(order);
        cartService.clearCart(buyerId);

        RazorPayOrderResponse orderResponse = paymentService.createRazorPayOrder(savedOrder);
        return OrderResponse.fromEntity(savedOrder, orderResponse.getOrderId(), key, null);
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
            item.setStatus(OrderItem.OrderItemStatus.CANCELLED);
            inventoryService.releaseStock(orderId, item.getProductId());
        }


        // Mark as cancelled and save
        order.setStatus(Orders.OrderStatus.CANCELLED);
        System.out.println("cancel4");

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void confirmOrderPayment(UUID orderId, UUID buyerId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != Orders.OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be confirmed");
        }

        // Tell the Vault to permanently deduct the physical stock
        for (OrderItem item : order.getOrderItems()) {
            inventoryService.confirmStock(orderId, item.getProductId());
            // sale ACTIVE hai to "claimed" bar badhao
            flashSaleService.recordSale(item.getProductId(), item.getQuantity());
            item.setStatus(OrderItem.OrderItemStatus.CONFIRMED);
        }

        order.setStatus(Orders.OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }

    @Override
    public OrdersResponseOfBuyer getOrderOfBuyer(UUID id, Pageable pageable, Orders.OrderStatus status) {
        if(id == null) throw new IllegalArgumentException("login first");

        List<Orders> orderList = orderRepository.findOrdersOfBuyer(id,pageable,status);


        OrdersResponseOfBuyer response = new OrdersResponseOfBuyer();
        response.setLast(true);
        response.setTotalElements(orderList.size());
        response.setTotalPages(1);
        response.setNumber(1);
        response.setSize(orderList.size());

        if(orderList.size() > 0) {
            List<OrderResponse> contentList = new ArrayList<>();
            for(Orders order: orderList) {
                List<OrderItemResponse> orderItemResponses = new ArrayList<>();
                for(OrderItem orderItem: order.getOrderItems()) {
                    orderItemResponses.add(OrderItemResponse.fromEntity(orderItem,
                            productService.getProductNameByProductId(orderItem.getProductId()),
                            productService.getProductImageByProductId(orderItem.getProductId())));
                }
                contentList.add(OrderResponse.fromEntity(order, paymentService.getRazorPayOrderId(order.getId()), key, null, orderItemResponses));
            }

            response.setContent(contentList);
        }
        return response;
    }

    @Override
    public List<SellerOrderResponse> getOrderRelatedToSeller(UUID id, int page, int size, OrderItem.OrderItemStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderItem> ordersPage = orderItemRepository.findOrdersOfSeller(id, status, pageable);

        List<OrderItem> ordersList = ordersPage.get().toList();

        List<SellerOrderResponse> sellerOrderResponses = new ArrayList<>();
        for(OrderItem oi: ordersList) {
            UUID productId = oi.getProductId();

            Product product = productService.findProductByIdOrThrow(productId);

            ProductDetails productDetails = ProductDetails.builder()
                    .productId(productId.toString())
                    .imageUrl(productService.getProductImageByProductId(productId))
                    .productName(productService.getProductNameByProductId(productId))
                    .quantity(oi.getQuantity())
                    .unitPrice(oi.getUnitPriceAtPurchase())
                    .subTotal(oi.getSubTotal())
                    .build();


            sellerOrderResponses.add(SellerOrderResponse.builder()
                    .orderId(oi.getId().toString())
                    .status(oi.getStatus().toString())
                    .item(productDetails)
                    .shippingAddress(oi.getOrder().getShippingAddressSnapshot())
                    .sellerTotal(oi.getSubTotal())
                    .build());
        }

        return sellerOrderResponses;
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateOrderStatus(UUID id, UUID orderId) {


        OrderItem orderItem = orderItemRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotAvailabe("orderItem Record Not found!!"));

        Product product = productService.findProductByIdOrThrow(orderItem.getProductId());
        if(product.getSellerId().compareTo(id) != 0) {
            throw new OrderNotAvailabe("Only Owner can perform this transition!!");
        }

        if(orderItem.getStatus().toString().equals("CONFIRMED")) orderItem.setStatus(OrderItem.OrderItemStatus.SHIPPED);
        else if(orderItem.getStatus().toString().equals("SHIPPED")) orderItem.setStatus(OrderItem.OrderItemStatus.DELIVERED);
        else throw new OrderNotAvailabe("Order is in " + orderItem.getStatus() + "status and bcz of this the transition is not possible");


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
