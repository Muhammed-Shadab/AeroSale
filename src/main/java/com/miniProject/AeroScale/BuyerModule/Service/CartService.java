package com.miniProject.AeroScale.BuyerModule.Service;

import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.CartResponse;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.UpdateCartItemRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CartService {

    void addItemToCart(ItemDataForCart itemDataForCart, UUID id);

    void removeItem(UUID itemId, UUID buyerId);

    List<CartResponse> getAllCarts(UUID id);

    void UpdateCartDetails(UpdateCartItemRequest updateCartItemRequest, UUID id);

    void clearCart(UUID id);
}
