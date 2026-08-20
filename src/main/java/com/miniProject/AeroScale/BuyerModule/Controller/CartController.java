package com.miniProject.AeroScale.BuyerModule.Controller;

import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.CartResponse;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.UpdateCartItemRequest;
import com.miniProject.AeroScale.BuyerModule.Service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/getCart")
    public ResponseEntity<List<CartResponse>> getAllCarts(@AuthenticationPrincipal(expression = "id") UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(cartService.getAllCarts(id));

    }

    @PostMapping("/addToCart")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody ItemDataForCart itemDataForCart,
                                           @AuthenticationPrincipal(expression = "id") UUID id) {
        cartService.addItemToCart(itemDataForCart, id);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @DeleteMapping("/deleteFromCart/{cartId}")
    public ResponseEntity<?> DeleteFromCart(@PathVariable UUID cartId, @AuthenticationPrincipal(expression = "id") UUID buyerId) {
        cartService.removeItem(cartId, buyerId);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PutMapping("/updateCart")
    public ResponseEntity<?> UpdateCartDetails(@AuthenticationPrincipal(expression = "id") UUID buyerId
                                               , @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        cartService.UpdateCartDetails(updateCartItemRequest, buyerId);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @DeleteMapping("/clearCart")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal(expression = "id") UUID id) {
        cartService.clearCart(id);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }



}
