package com.miniProject.AeroScale.SellerModule.Service.Impl;

import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.BuyerModule.Exception.RequiredThingsNotFoundException;
import com.miniProject.AeroScale.SellerModule.DTO.Request.UpdateProfileRequest;
import com.miniProject.AeroScale.SellerModule.DTO.Response.ProfileResponse;
import com.miniProject.AeroScale.SellerModule.Entity.Seller;
import com.miniProject.AeroScale.SellerModule.Repository.SellerRepository;
import com.miniProject.AeroScale.SellerModule.Service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public void updateProfile(UpdateProfileRequest updateProfileRequest, UUID id) {
        Seller seller = sellerRepository.findById(id).get();

        seller.setDateOfBirth(updateProfileRequest.getDOB());
        seller.setBussinessName(updateProfileRequest.getBussinessName());

        if(updateProfileRequest.getBussinessAddress() != null) seller.setBussinessAddress(updateProfileRequest.getBussinessAddress());
        if(updateProfileRequest.getWareHousePincode() != null) seller.setWareHousePinCode(updateProfileRequest.getWareHousePincode());

        sellerRepository.save(seller);
    }

    @Override
    public ProfileResponse getProfile(UUID id, String email) {
        Seller seller = findSellerOrThrow(id);

        return ProfileResponse.builder()
                .id(id.toString())
                .email(email)
                .bussinessName(seller.getBussinessName())
                .bussinessAddress(seller.getBussinessAddress())
                .dob(seller.getDateOfBirth())
                .verificationStatus(seller.getSellerVerificationStatus().toString())
                .rating(seller.getRating())
                .totalRevenue(seller.getTotalRevenew())
                .maxConcurrentFlashSaleSlots(seller.getMaxConcurrentFlashSaleSlots())
                .createdAt(seller.getCreatedAt())
                .build();
    }

    private Seller findSellerOrThrow(UUID id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new RequiredThingsNotFoundException("User Not Found"));
    }
}
