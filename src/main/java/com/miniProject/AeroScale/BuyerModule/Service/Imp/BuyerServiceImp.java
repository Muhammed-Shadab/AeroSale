package com.miniProject.AeroScale.BuyerModule.Service.Imp;

import com.miniProject.AeroScale.BuyerModule.DTO.Request.AddAddressRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Request.BuyerProfileUpdateRequest;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.AddAddressResponse;
import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.BuyerModule.Entity.BuyerAddress;
import com.miniProject.AeroScale.BuyerModule.Exception.RequiredThingsNotFoundException;
import com.miniProject.AeroScale.BuyerModule.Repository.BuyerAddressRepository;
import com.miniProject.AeroScale.BuyerModule.Repository.BuyerRepository;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuyerServiceImp implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final BuyerAddressRepository buyerAddressRepository;

    @Override
    @Transactional
    public void updateProfile(BuyerProfileUpdateRequest buyerProfileUpdateRequest,AuthenticatedObject authenticatedObject) {
        UUID id = authenticatedObject.id();
        Buyer buyer = findBuyerOrThrow(authenticatedObject.id());

        buyer.setFullName(buyerProfileUpdateRequest.getFullName());
        buyer.setDateOfBirth(buyerProfileUpdateRequest.getDob());

        buyerRepository.save(buyer);
    }

    @Override
    @Transactional
    public AddAddressResponse addAddress(AddAddressRequest addAddressRequest, UUID id) {
        Buyer buyer = findBuyerOrThrow(id);

        buyerAddressRepository.clearDefaultForBuyer(id);

        BuyerAddress buyerAddress = BuyerAddress.builder()
                .buyer(buyer)
                .addressLabel(addAddressRequest.getLabel())
                .addressLine1(addAddressRequest.getAddressLine1())
                .addressLine2(addAddressRequest.getAddressLine2())
                .city(addAddressRequest.getCity())
                .country(addAddressRequest.getCountry())
                .state(addAddressRequest.getState())
                .pincode(addAddressRequest.getPincode())
                .isDefault(addAddressRequest.isDefault())
                .recipientName(addAddressRequest.getRecipientName())
                .recipientPhoneNo(addAddressRequest.getPhoneNo())
                .build();


        BuyerAddress savedAddress = buyerAddressRepository.save(buyerAddress);
        return BuyerAddressToAddAddressResponseConverter(savedAddress);
    }

    private AddAddressResponse BuyerAddressToAddAddressResponseConverter(BuyerAddress buyerAddress) {
        return AddAddressResponse.builder()
                .addressLine1(buyerAddress.getAddressLine1())
                .addressLine2(buyerAddress.getAddressLine2())
                .city(buyerAddress.getCity())
                .state(buyerAddress.getState())
                .country(buyerAddress.getCountry())
                .pincode(buyerAddress.getPincode())
                .id(buyerAddress.getId())
                .createdAt(buyerAddress.getCreatedAt())
                .updatedAt(buyerAddress.getUpdatedAt())
                .isDefault(buyerAddress.isDefault())
                .recipientName(buyerAddress.getRecipientName())
                .recipientPhoneNo(buyerAddress.getRecipientPhoneNo())
                .build();

    }

    private Buyer findBuyerOrThrow(UUID id) {
        return buyerRepository.findById(id)
                .orElseThrow(() -> new RequiredThingsNotFoundException("User Not Found"));
    }
}
