package com.miniProject.AeroScale.SellerModule.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.Before;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateProfileRequest {

    @NotBlank(message = "BussinessName is Required")
    private String bussinessName;

    @Past(message = "Date of Birth Should be in past")
    @NotNull(message = "DOB is Required")
    private Instant DOB;

    private String bussinessAddress;
    private String wareHousePincode;

}
