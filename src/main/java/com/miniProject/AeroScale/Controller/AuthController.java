package com.miniProject.AeroScale.Controller;


import com.miniProject.AeroScale.DTO.Request.LoginRequest;
import com.miniProject.AeroScale.DTO.Request.RegisterRequest;
import com.miniProject.AeroScale.DTO.Response.RegisterResponse;
import com.miniProject.AeroScale.Service.AuthService;
import com.miniProject.AeroScale.Service.AuthServiceImp;
import jakarta.annotation.PostConstruct;
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

}
