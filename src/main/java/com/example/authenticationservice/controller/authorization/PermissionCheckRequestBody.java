package com.example.authenticationservice.controller.authorization;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class PermissionCheckRequestBody {
    @NotBlank
    private String roleToHave;
}
