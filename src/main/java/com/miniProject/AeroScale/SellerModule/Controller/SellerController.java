package com.miniProject.AeroScale.SellerModule.Controller;

import com.miniProject.AeroScale.SellerModule.DTO.Request.UpdateProfileRequest;
import com.miniProject.AeroScale.SellerModule.DTO.Response.ProfileResponse;
import com.miniProject.AeroScale.SellerModule.Service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
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

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal(expression = "id") UUID id ,
                                                    @AuthenticationPrincipal(expression= "email") String email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.getProfile(id, email));
    }

}
