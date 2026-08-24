package com.miniProject.AeroScale.SellerModule.Controller;

import com.miniProject.AeroScale.SellerModule.DTO.Request.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest,
                                           @AuthenticationPrincipal(expression = "id") UUID id) {
        return ResponseEntity.ok().build();
    }
}
