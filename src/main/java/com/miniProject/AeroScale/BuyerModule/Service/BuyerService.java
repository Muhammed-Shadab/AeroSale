package com.miniProject.AeroScale.BuyerModule.Service;


import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.AddAddressRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.BuyerProfileUpdateRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;

import java.security.Principal;
import java.util.UUID;

@Service
public interface BuyerService {


    void updateProfile(BuyerProfileUpdateRequest buyerProfileUpdateRequest, AuthenticatedObject authenticatedObject);

    void addAddress(AddAddressRequest addAddressRequest, UUID id);
}
