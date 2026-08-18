package com.miniProject.AeroScale.BuyerModule.Service;

import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;

@Service
public interface CartService {

    void addItemToCart(ItemDataForCart itemDataForCart, AuthenticatedObject authenticatedObject);
}
