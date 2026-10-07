package com.miniProject.AeroScale.Payment;

import com.miniProject.AeroScale.Payment.Entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TransactionsRepository extends JpaRepository<Transactions, UUID> {

    Optional<String> findByOrderId(java.util.UUID id);

    @Query("SELECT t FROM Transactions t WHERE t.orderId = :id")
    Optional<Transactions> findTransactionByOrderId(@Param("id") UUID id);

}
