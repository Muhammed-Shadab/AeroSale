package com.miniProject.AeroScale.Service;

import com.miniProject.AeroScale.DTO.Request.LoginRequest;
import com.miniProject.AeroScale.DTO.Request.RefreshTokenRequest;
import com.miniProject.AeroScale.DTO.Request.RegisterRequest;
import com.miniProject.AeroScale.DTO.Response.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    public RegisterResponse register(RegisterRequest registerRequest);

    public RegisterResponse login(LoginRequest loginRequest);

    RegisterResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
