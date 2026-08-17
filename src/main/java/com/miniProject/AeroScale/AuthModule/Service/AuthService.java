package com.miniProject.AeroScale.AuthModule.Service;

import com.miniProject.AeroScale.AuthModule.DTO.Request.LoginRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Request.RefreshTokenRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Request.RegisterRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Response.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    public RegisterResponse register(RegisterRequest registerRequest);

    public RegisterResponse login(LoginRequest loginRequest);

    RegisterResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
