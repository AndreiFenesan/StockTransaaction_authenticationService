package com.example.authenticationservice.controller.authorization;

import com.example.authenticationservice.service.authorization.AuthorizationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authorization")
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<PermissionResponseBody> checkPermission(
            @Valid @NotBlank @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader,
            @Valid @RequestBody PermissionCheckRequestBody permissionCheckRequestBody) {

        var userId = authorizationService.
                checkPermission(authHeader, permissionCheckRequestBody.getRoleToHave());
        log.info("Authorized: {}", userId);
        if (userId != null) {
            return ResponseEntity.ok(new PermissionResponseBody(userId));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
