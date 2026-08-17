package com.miniProject.AeroScale.AuthModule.DTO.Response;

import com.miniProject.AeroScale.AuthModule.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
