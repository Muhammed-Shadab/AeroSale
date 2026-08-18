package com.miniProject.AeroScale.BuyerModule.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuyerProfileUpdateRequest {

    @NotBlank(message = "FullName should not be empty")
    private String fullName;

    @NotNull
    @Past(message = "Date of birth should be in past")
    private Instant Dob;

}
