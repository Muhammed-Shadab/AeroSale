package com.miniProject.AeroScale.SellerModule.Service;

import com.miniProject.AeroScale.SellerModule.DTO.Request.UpdateProfileRequest;
import com.miniProject.AeroScale.SellerModule.DTO.Response.AnalyticsResponse;
import com.miniProject.AeroScale.SellerModule.DTO.Response.ProfileResponse;
import com.miniProject.AeroScale.SellerModule.Entity.Seller;
import jakarta.validation.Valid;

import java.util.UUID;

public interface SellerService {
    void updateProfile(UpdateProfileRequest updateProfileRequest, UUID id);

    ProfileResponse getProfile(UUID id, String email);

    Seller findSellerOrThrow(UUID id);

    AnalyticsResponse getDataForAnalytics(UUID id, int days);
}
