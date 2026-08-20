package com.miniProject.AeroScale.BuyerModule.DTO.Response;

import com.miniProject.AeroScale.BuyerModule.Entity.BuyerAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddAddressResponse {

    private UUID id;
    private BuyerAddress.AddressLabel label;
    private String recipientName;
    private String recipientPhoneNo;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private boolean isDefault;
    private Instant createdAt;
    private Instant updatedAt;
}
