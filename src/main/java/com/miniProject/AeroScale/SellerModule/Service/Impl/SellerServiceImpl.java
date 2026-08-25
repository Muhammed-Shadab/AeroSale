package com.miniProject.AeroScale.SellerModule.Service.Impl;

import com.miniProject.AeroScale.SellerModule.DTO.Request.UpdateProfileRequest;
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
}
