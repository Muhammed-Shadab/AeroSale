package com.miniProject.AeroScale.Controller;


import com.miniProject.AeroScale.DTO.Request.RegisterRequest;
import com.miniProject.AeroScale.DTO.Response.RegisterResponse;
import com.miniProject.AeroScale.Service.AuthService;
import com.miniProject.AeroScale.Service.AuthServiceImp;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

}
