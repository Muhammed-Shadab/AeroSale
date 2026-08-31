package com.miniProject.AeroScale.SellerModule.Controller;

import com.miniProject.AeroScale.SellerModule.DTO.Request.UpdateProfileRequest;
import com.miniProject.AeroScale.SellerModule.Service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest,
                                           @AuthenticationPrincipal(expression = "id") UUID id) {
        sellerService.updateProfile(updateProfileRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PostMapping("/uodateBankDetails")
    public ResponseEntity<?> updateBankDetails() [
            yes doing an empty commit dont look bitch;
            yes doing an another empty commit dont look bitch;
            ]

}
