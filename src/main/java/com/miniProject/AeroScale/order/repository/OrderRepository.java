package com.miniProject.AeroScale.order.repository;

import com.miniProject.AeroScale.order.entity.OrderItem;
import com.miniProject.AeroScale.order.entity.Orders;
import com.razorpay.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {

    // Allows a buyer to view their past orders securely
    Page<Orders> findAllByBuyerId(UUID buyerId, Pageable pageable);

    // Idempotency check
    Optional<Orders> findByIdempotencyKey(String idempotencyKey);

    // Secure fetch for cancellation
    Optional<Orders> findByIdAndBuyerId(UUID id, UUID buyerId);

    @Query("""
    SELECT o FROM Orders o
    WHERE (:status IS NULL OR o.status = :status)
    AND o.buyerId = :id
""")
    List<Orders> findOrdersOfBuyer(
            @Param("id") UUID id,
            Pageable pageable,
            @Param("status") Orders.OrderStatus status
    );

//
//    @Query("""
//            update Orders o
//            set o.status = :actualStatus
//            where o.id = :orderId and o.
//            """)
//    void changeTheOrderStatus(UUID id, UUID orderId, Orders.OrderStatus actualStatus);
}