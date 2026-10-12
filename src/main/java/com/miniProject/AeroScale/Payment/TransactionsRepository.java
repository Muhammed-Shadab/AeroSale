package com.miniProject.AeroScale.Payment;

import com.miniProject.AeroScale.Payment.Entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionsRepository extends JpaRepository<Transactions, UUID> {

    Optional<Transactions> findByOrderId(UUID orderId);

    Optional<Transactions> findByRazorpayOrderId(String razorpayOrderId);
}