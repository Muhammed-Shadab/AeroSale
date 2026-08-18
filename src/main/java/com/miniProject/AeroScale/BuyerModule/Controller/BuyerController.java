package com.miniProject.AeroScale.BuyerModule.Controller;


import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.BuyerProfileUpdateRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
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

@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody BuyerProfileUpdateRequest buyerProfileUpdateRequest,
                                           @AuthenticationPrincipal AuthenticatedObject authenticatedObject) {

            buyerService.updateProfile(buyerProfileUpdateRequest, authenticatedObject);
            return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

}
