package com.miniProject.AeroScale.BuyerModule.DTO.Request;

import com.miniProject.AeroScale.BuyerModule.Entity.BuyerAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddAddressRequest {

    @NotNull
    private BuyerAddress.AddressLabel label;

    @NotBlank(message = "Recipient Name is Required")
    private String recipientName;

    @NotBlank(message = "Recipient Phone number is Required")
    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "Phone number must be a valid E.164 number")
    private String PhoneNo;

    @NotBlank(message = "Address Is Required")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City is Required")
    private String city;

    @NotBlank(message = "State is Required")
    private String state;

    @NotBlank(message = "Country is Required")
    private String country;

    @NotBlank(message = "Pincode is Required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pincode must be a valid 6-digit Indian pincode")
    private String pincode;

    @Builder.Default
    private boolean isDefault = false;
}
