package com.miniProject.AeroScale.Payment;

import com.miniProject.AeroScale.Payment.Entity.Transactions;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<Transactions, UUID> {

    Optional<String> findByOrderId(java.util.UUID id);
}
