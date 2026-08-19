package com.miniProject.AeroScale.BuyerModule.Repository;

import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.BuyerModule.Entity.CartItem;
import com.miniProject.AeroScale.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRespository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByBuyerAndProduct(Buyer buyer, Product product);

    Optional<CartItem> findByIdAndBuyerId(UUID id, UUID buyerid);

    List<CartItem> findAllByBuyerId(UUID buyerid);

    void deleteAllByBuyerId(UUID buyerid);
}
