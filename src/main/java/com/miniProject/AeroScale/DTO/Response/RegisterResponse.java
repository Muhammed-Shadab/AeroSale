package com.miniProject.AeroScale.DTO.Response;

import com.miniProject.AeroScale.Entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RegisterResponse {

    private UUID UUID;
    private String email;
    private Role role;

    @Builder.Default
    private String tokenType = "Bearer";

    private String accessToken;
    private String refreshToken;
    private Long ExpiresInSec;
}
