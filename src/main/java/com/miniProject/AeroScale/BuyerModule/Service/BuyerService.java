package com.miniProject.AeroScale.BuyerModule.Service;


import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.AddAddressRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.BuyerProfileUpdateRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.AddAddressResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BuyerService {


    void updateProfile(BuyerProfileUpdateRequest buyerProfileUpdateRequest, AuthenticatedObject authenticatedObject);

    AddAddressResponse addAddress(AddAddressRequest addAddressRequest, UUID id);

//    this is for cross-module communication (Order -> Buyer)
    AddAddressResponse getBuyerAddressForCheckout(UUID buyerId, UUID addressId);

    List<AddAddressResponse> getAllAddress(UUID id);

    void deleteAddress(UUID id, UUID addId);
}
