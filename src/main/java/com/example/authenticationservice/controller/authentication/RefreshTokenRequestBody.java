package com.example.authenticationservice.controller.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RefreshTokenRequestBody {
    @NotBlank(message = "Refresh token shouldn't be blank")
    @Size(min = 1, max = 300)
    private String refreshToken;
}
