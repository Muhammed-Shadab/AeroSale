package com.miniProject.AeroScale.AuthModule.Controller.Repository;

import com.miniProject.AeroScale.AuthModule.Controller.Entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, UUID> {
}
