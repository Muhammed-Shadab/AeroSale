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

import java.util.ArrayList;
import java.util.List;
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
    public AddAddressResponse  addAddress(AddAddressRequest addAddressRequest, UUID id) {
        Buyer buyer = findBuyerOrThrow(id);
        int count = buyerAddressRepository.countByBuyerId(id);

        if(count == 0) addAddressRequest.setDefault(true);
        else if(addAddressRequest.isDefault()) buyerAddressRepository.clearDefaultForBuyer(id);
        System.out.println(addAddressRequest.getLabel());
        BuyerAddress buyerAddress = BuyerAddress.builder()
                .addressLabel(addAddressRequest.getLabel())
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

    @Override
    @Transactional(readOnly = true)
    public AddAddressResponse getBuyerAddressForCheckout(UUID buyerId, UUID addressId) {
        BuyerAddress address = buyerAddressRepository.findById(addressId)
                .orElseThrow(() -> new RequiredThingsNotFoundException("Shipping address not found"));

        if (!address.getBuyer().getId().equals(buyerId)) {
            throw new RequiredThingsNotFoundException("Address does not belong to the current buyer");
        }

        return BuyerAddressToAddAddressResponseConverter(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddAddressResponse> getAllAddress(UUID id) {
        List<BuyerAddress> addressList = buyerAddressRepository.findAllByBuyerId(id);

        List<AddAddressResponse> responseList = new ArrayList<>();
        for(BuyerAddress address: addressList) {
            responseList.add(BuyerAddressToAddAddressResponseConverter(address));
        }
        return responseList;
    }

    @Override
    @Transactional
    public void deleteAddress(UUID id, UUID addId) {
        // default wala thing is not handle handle it in the order service or here only
        BuyerAddress address = findAddressByBuyerOrThrow(addId, id);

        buyerAddressRepository.delete(address);
    }

    @Override
    @Transactional
    public void updateAddress(UUID id, UUID addId, AddAddressRequest newAddress) {
        BuyerAddress address = findAddressByBuyerOrThrow(addId, id);

        if(newAddress.isDefault() && !address.isDefault()) buyerAddressRepository.clearDefaultForBuyer(id);
        System.out.println(newAddress.getLabel());
        address.setAddressLabel(newAddress.getLabel());
        address.setRecipientName(newAddress.getRecipientName());
        address.setRecipientPhoneNo(newAddress.getPhoneNo());
        address.setAddressLine1(newAddress.getAddressLine1());
        address.setAddressLine2(newAddress.getAddressLine2());
        address.setCity(newAddress.getCity());
        address.setState(newAddress.getState());
        address.setCountry(newAddress.getCountry());
        address.setPincode(newAddress.getPincode());
        address.setDefault(newAddress.isDefault());

        buyerAddressRepository.save(address);
    }

    private BuyerAddress findAddressByBuyerOrThrow(UUID addId, UUID id) {
        return buyerAddressRepository.findByIdAndBuyerId(addId, id)
                .orElseThrow(() -> new RequiredThingsNotFoundException("Address Not Found!!"));
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
