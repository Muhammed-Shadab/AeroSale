package com.miniProject.AeroScale.Service;

import com.miniProject.AeroScale.DTO.Request.RegisterRequest;
import com.miniProject.AeroScale.DTO.Response.RegisterResponse;

public interface AuthService {

    public RegisterResponse register(RegisterRequest registerRequest);
}
