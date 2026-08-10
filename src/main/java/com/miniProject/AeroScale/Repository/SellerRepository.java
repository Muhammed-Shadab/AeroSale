package com.miniProject.AeroScale.Repository;

import com.miniProject.AeroScale.Entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
}
