package com.miniProject.AeroScale.DTO.Request;


import com.miniProject.AeroScale.Entity.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Builder
@Data
public class RegisterRequest {

    @NotBlank(message = "Email should not be blank!!")
    @Email(message = "Enter a valid email")
    @Size(max = 254)
    private String email;

    @NotBlank(message = "password Should not be blank!!")
    @Size(min = 8, max = 19, message = "Password must be between 8 and 19 chracters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character"
    )
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "Phone number must be a valid E.164 number")
    private String phoneNo;

    @NotNull(message = "Enter the role")
    private Role role;


    @NotBlank(message = "FullName/BussinessName is required!!")
    @Size(max = 250)
    private String fullName;

    @NotNull(message = "Enter the date of Birth")
    private Instant DOB;

    //buyer specific
    private String defaultShippingAddress;
    private String defaultShippingPincode;

    //seller specific
    private String bussinessAdress;
    private String wareHousePinCode;



}
