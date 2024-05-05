package com.example.authenticationservice.service;

import com.example.authenticationservice.controller.authentication.CredentialsRequestBody;
import com.example.authenticationservice.controller.authentication.AuthenticationResponseBody;
import com.example.authenticationservice.controller.authentication.RefreshTokenRequestBody;
import com.example.authenticationservice.datasource.model.Roles;
import com.example.authenticationservice.datasource.model.User;
import com.example.authenticationservice.exception.InvalidTokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final String ROLE_CLAIM = "role";
    private static final String USER_ID_CLAIM = "userId";
    public static final String TOKEN_TYPE_USER = "user";
    private static final String TOKEN_TYPE_REFRESH = "refresh";
    public static final String TOKEN_TYPE_NAME = "type";

    public AuthenticationResponseBody authenticateUser(CredentialsRequestBody credentialsRequestBody) {
        final String userEmail = credentialsRequestBody.getUserEmail();
        final String userPassword = credentialsRequestBody.getPassword();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, userPassword));
        UserDetails user = userDetailsService.loadUserByUsername(userEmail);
        return getAuthenticationResponse(user);
    }

    public AuthenticationResponseBody refreshToken(UserDetails principal, RefreshTokenRequestBody refreshTokenRequestBody) {
        String refreshToken = refreshTokenRequestBody.getRefreshToken();
        String tokenType = jwtService.getTokenType(refreshToken);
        if (!TOKEN_TYPE_REFRESH.equals(tokenType) || !jwtService.isTokenValid(refreshToken, principal)) {
            throw new InvalidTokenType("Not a refresh token");
        }

        return getAuthenticationResponse(principal);
    }

    private AuthenticationResponseBody getAuthenticationResponse(UserDetails principal) {
        User user = (User) principal;
        var extraClaims = new HashMap<String, Object>();
        extraClaims.put(ROLE_CLAIM, getRoleName(principal));
        extraClaims.put(USER_ID_CLAIM, user.getId());
        final String jwt = generateJwtUserToken(extraClaims, principal);
        final String refreshToken = generateJwtRefreshToken(extraClaims, principal);

        return AuthenticationResponseBody.builder()
                .authenticationToken(jwt)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .role(Roles.BASIC_USER)
                .build();
    }

    private Object getRoleName(UserDetails user) {
        SimpleGrantedAuthority userAuthority = (SimpleGrantedAuthority) (user.getAuthorities().toArray()[0]);
        return userAuthority.getAuthority();
    }

    private String generateJwtUserToken(Map<String, Object> extraClaims, UserDetails user) {
        Map<String, Object> extraClaimsCopy = new HashMap<>(extraClaims);
        extraClaimsCopy.put(TOKEN_TYPE_NAME, TOKEN_TYPE_USER);
        return jwtService.generateJwtToken(extraClaimsCopy, user);
    }

    private String generateJwtRefreshToken(Map<String, Object> extraClaims, UserDetails user) {
        Map<String, Object> extraClaimsCopy = new HashMap<>(extraClaims);
        extraClaimsCopy.put(TOKEN_TYPE_NAME, TOKEN_TYPE_REFRESH);
        return jwtService.generateJwtToken(extraClaimsCopy, user);
    }

}
