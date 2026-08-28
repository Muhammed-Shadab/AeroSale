package com.miniProject.AeroScale.SellerModule.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateBankDetailsRequest {

    private String gstin;

    private String BankAccountNumber;

    private String BankIfscCode;

}
