package com.miniProject.AeroScale.SellerModule.Repository;

import com.miniProject.AeroScale.SellerModule.Entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
}
