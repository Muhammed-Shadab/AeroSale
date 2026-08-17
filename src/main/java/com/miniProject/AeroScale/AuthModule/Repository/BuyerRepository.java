package com.miniProject.AeroScale.AuthModule.Repository;

import com.miniProject.AeroScale.AuthModule.Entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, UUID> {
}
