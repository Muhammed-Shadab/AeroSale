package com.miniProject.AeroScale.AuthModule.Controller;


import com.miniProject.AeroScale.AuthModule.DTO.Request.LoginRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Request.RefreshTokenRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Request.RegisterRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Response.RegisterResponse;
import com.miniProject.AeroScale.AuthModule.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final  AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<RegisterResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        RegisterResponse registerResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<RegisterResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        RegisterResponse registerResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        authService.logout(refreshTokenRequest);
        return ResponseEntity.noContent().build();
    }
}
