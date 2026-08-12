package com.miniProject.AeroScale.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    @NotBlank(message = "Email should not be empty!")
    @Email(message = "Enter a valid email")
    @Size(max = 50)
    private String email;

    @NotBlank(message = "Password should not be empty!")
    @Size(min = 8, max = 19, message = "Password must be between 8 and 19 chracters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character"
    )
    private String password;
}
