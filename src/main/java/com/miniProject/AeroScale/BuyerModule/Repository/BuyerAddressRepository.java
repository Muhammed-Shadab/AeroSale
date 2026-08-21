package com.miniProject.AeroScale.BuyerModule.Repository;

import com.miniProject.AeroScale.BuyerModule.Entity.BuyerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BuyerAddressRepository extends JpaRepository<BuyerAddress, UUID> {

    @Modifying
    @Query("UPDATE BuyerAddress b SET b.isDefault = false WHERE b.buyer.id = :id AND b.isDefault = true")
    void clearDefaultForBuyer(UUID id);

    int countByBuyerId(UUID id);

    List<BuyerAddress> findAllByBuyerId(UUID id);

    Optional<BuyerAddress> findByIdAndBuyerId(UUID addId, UUID id);
}
