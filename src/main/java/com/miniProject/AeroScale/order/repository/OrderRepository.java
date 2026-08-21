package com.miniProject.AeroScale.order.repository;

import com.miniProject.AeroScale.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {

    // Allows a buyer to view their past orders securely
    Page<Orders> findAllByBuyerId(UUID buyerId, Pageable pageable);
}