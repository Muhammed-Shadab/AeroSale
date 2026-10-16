package com.miniProject.AeroScale.SellerModule.Service;

import com.miniProject.AeroScale.SellerModule.DTO.Request.UpdateProfileRequest;
import com.miniProject.AeroScale.SellerModule.DTO.Response.ProfileResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface SellerService {
    void updateProfile(UpdateProfileRequest updateProfileRequest, UUID id);

    ProfileResponse getProfile(UUID id, String email);
}
