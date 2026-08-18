package com.miniProject.AeroScale.BuyerModule.Repository;

import com.miniProject.AeroScale.BuyerModule.Entity.BuyerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BuyerAddressRepository extends JpaRepository<BuyerAddress, UUID> {

}
