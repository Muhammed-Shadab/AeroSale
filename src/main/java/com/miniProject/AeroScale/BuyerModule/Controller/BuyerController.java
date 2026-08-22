package com.miniProject.AeroScale.BuyerModule.Controller;


import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.AddAddressRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.BuyerProfileUpdateRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.AddAddressResponse;
import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody BuyerProfileUpdateRequest buyerProfileUpdateRequest,
                                           @AuthenticationPrincipal(expression = "id") AuthenticatedObject authenticatedObject) {

            buyerService.updateProfile(buyerProfileUpdateRequest, authenticatedObject);
            return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PostMapping("/addAddress")
    public ResponseEntity<AddAddressResponse> addAddress(@Valid @RequestBody AddAddressRequest addAddressRequest,
                                                         @AuthenticationPrincipal(expression = "id") UUID id) {
        buyerService.addAddress(addAddressRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping("/getAllAddress")
    public ResponseEntity<List<AddAddressResponse>> getAllAddress(@AuthenticationPrincipal(expression = "id") UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(buyerService.getAllAddress(id));
    }

    @DeleteMapping("/deleteAddress/{addId}")
    public ResponseEntity<?> deleteAddress(@AuthenticationPrincipal(expression = "id") UUID id
                                         , @PathVariable UUID addId) {
        buyerService.deleteAddress(id, addId);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PutMapping("/updateAddress/{addId}")
    public ResponseEntity<?> updateAddress(@AuthenticationPrincipal(expression = "id") UUID id
                                            , @PathVariable UUID addId
                                            , @Valid @RequestBody AddAddressRequest newAddress) {
        buyerService.updateAddress(id, addId, newAddress);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }




}
