package com.miniProject.AeroScale.order.repository;

import com.miniProject.AeroScale.order.entity.OrderItem;
import com.miniProject.AeroScale.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {


    OrderItem findByOrder(Orders order);
}
