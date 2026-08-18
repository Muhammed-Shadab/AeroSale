package com.miniProject.AeroScale.BuyerModule.Service.Imp;

import com.miniProject.AeroScale.BuyerModule.DTO.Request.BuyerProfileUpdateRequest;
import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.BuyerModule.Exception.RequiredThingsNotFoundException;
import com.miniProject.AeroScale.BuyerModule.Repository.BuyerRepository;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuyerServiceImp implements BuyerService {

    private final BuyerRepository buyerRepository;

    @Override
    public void updateProfile(BuyerProfileUpdateRequest buyerProfileUpdateRequest,AuthenticatedObject authenticatedObject) {
        UUID id = authenticatedObject.id();
        Buyer buyer = findBuyerOrThrow(authenticatedObject.id());

        buyer.setFullName(buyerProfileUpdateRequest.getFullName());
        buyer.setDateOfBirth(buyerProfileUpdateRequest.getDob());

        buyerRepository.save(buyer);
    }

    private Buyer findBuyerOrThrow(UUID id) {
        return buyerRepository.findById(id)
                .orElseThrow(() -> new RequiredThingsNotFoundException("User Not Found"));
    }
}
