package com.miniProject.AeroScale.SellerModule.Service;

import com.miniProject.AeroScale.SellerModule.DTO.Request.UpdateProfileRequest;
import jakarta.validation.Valid;

import java.util.UUID;

public interface SellerService {
    void updateProfile(UpdateProfileRequest updateProfileRequest, UUID id);
}
