package com.miniProject.AeroScale.Repository;

import com.miniProject.AeroScale.Entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuyerRepository extends JpaRepository<Buyer, UUID> {
}
