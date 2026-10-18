package com.miniProject.AeroScale.order.repository;

import com.miniProject.AeroScale.order.entity.OrderItem;
import com.miniProject.AeroScale.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {


    OrderItem findByOrder(Orders order);


    @Query("""
    SELECT oi
    FROM OrderItem oi
    JOIN Product p ON oi.productId = p.id
    WHERE (oi.status <> 'PENDING' OR oi.status = :status)
      AND p.sellerId = :id
    """)
    Page<OrderItem> findOrdersOfSeller(UUID id, OrderItem.OrderItemStatus status, Pageable pageable);
}
