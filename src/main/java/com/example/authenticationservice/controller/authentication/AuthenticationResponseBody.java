package com.example.authenticationservice.controller.authentication;

import com.example.authenticationservice.datasource.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthenticationResponseBody {
    private String authenticationToken;
    private String refreshToken;
    private int userId;
    private Roles role;

    public AuthenticationResponseBody() {
    }
}
