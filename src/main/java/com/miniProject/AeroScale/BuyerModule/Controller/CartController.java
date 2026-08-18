package com.miniProject.AeroScale.BuyerModule.Controller;

import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import com.miniProject.AeroScale.BuyerModule.Service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody ItemDataForCart itemDataForCart,
                                           @AuthenticationPrincipal AuthenticatedObject authenticatedObject) {
        cartService.addItemToCart(itemDataForCart, authenticatedObject);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }
}
