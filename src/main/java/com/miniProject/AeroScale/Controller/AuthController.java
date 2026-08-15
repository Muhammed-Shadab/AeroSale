package com.miniProject.AeroScale.Controller;


import com.miniProject.AeroScale.DTO.Request.LoginRequest;
import com.miniProject.AeroScale.DTO.Request.RefreshTokenRequest;
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
//   "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMTg3NmQ1Zi01YmZkLTQ4NzAtOTVmYy0zY2I4ZmY2OGM1ZTgiLCJlbWFpbCI6InM9cXFyYTRoeWhyb0BnbWFpbC5jb20iLCJyb2xlIjoiU0VMTEVSIiwiaXNzIjoiQWVyb1NjYWxlLWF1dGgtc2VydmljZSIsImlhdCI6MTc4Njc5ODY2NiwiZXhwIjoxNzg2Nzk5NTY2fQ.t8WAsFXf6XsVdEi-ZsxzFazZne7O2U3T8jYxnmXAtFY",
//           "refreshToken": "238soFu9uuKJPMjPrEqHEGRhz1UD-vgrBmQMVKEOKR5fMYeEEeH4bbPRlMH3FWB3l80-x5GV7F6wLehbOAJG7w",
    @GetMapping("/testing")
    public String test() {
        return "Working";
    }

}
