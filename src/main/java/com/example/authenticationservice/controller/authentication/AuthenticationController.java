package com.example.authenticationservice.controller.authentication;

import com.example.authenticationservice.exception.EmailAlreadyTakenException;
import com.example.authenticationservice.exception.InvalidTokenType;
import com.example.authenticationservice.service.AuthenticationService;
import com.example.authenticationservice.service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseBody> login(
            @Valid @RequestBody CredentialsRequestBody credentialsRequestBody) {

        AuthenticationResponseBody body = authenticationService
                .authenticateUser(credentialsRequestBody);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody CredentialsRequestBody credentialsRequestBody) {

        userRegistrationService.register(credentialsRequestBody);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponseBody> refreshToken(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody RefreshTokenRequestBody refreshTokenRequestBody) {

        AuthenticationResponseBody body = authenticationService
                .refreshToken(principal, refreshTokenRequestBody);
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> authenticationExceptionHandler(BadCredentialsException badCredentialsException) {
        return Map.of("error", badCredentialsException.getMessage());
    }

    @ExceptionHandler(InvalidTokenType.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidTokenTypeExceptionHandler(InvalidTokenType invalidTokenType) {
        return Map.of("error", invalidTokenType.getMessage());
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> emailAlreadyTakenExceptionHandler(EmailAlreadyTakenException emailAlreadyTakenException) {
        return Map.of("error", emailAlreadyTakenException.getMessage());
    }


}
