package com.miniProject.AeroScale.BuyerModule.Repository;

import com.miniProject.AeroScale.BuyerModule.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRespository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByBuyerAndProductId(UUID buyerId, UUID ProductId);

    Optional<CartItem> findByIdAndBuyerId(UUID itemId, UUID buyerId);

    List<CartItem> findAllByBuyerId(UUID id);

    void deleteAllByBuyerId(UUID id);
}
