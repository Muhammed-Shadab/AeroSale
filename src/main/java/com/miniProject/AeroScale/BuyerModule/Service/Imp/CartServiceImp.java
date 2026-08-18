package com.miniProject.AeroScale.BuyerModule.Service.Imp;

import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.BuyerModule.Exception.BuyerNotFoundException;
import com.miniProject.AeroScale.BuyerModule.Repository.BuyerRepository;
import com.miniProject.AeroScale.BuyerModule.Repository.CartRespository;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import com.miniProject.AeroScale.BuyerModule.Service.CartService;
import com.miniProject.AeroScale.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {

    private final CartRespository cartRespository;
    private final BuyerService buyerService;
    private final BuyerRepository buyerRepository;


    @Override
    public void addItemToCart(ItemDataForCart itemDataForCart, AuthenticatedObject authenticatedObject) {
        Buyer buyer = buyerRepository.findById(authenticatedObject.id()).
                orElseThrow(() -> new BuyerNotFoundException("To add items in cart buyer id is required"));

    }
}
