package com.example.authenticationservice.service.authorization;

import com.example.authenticationservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public String checkPermission(String authToken, String roleToHave) {
        if (authToken == null || !authToken.startsWith("Bearer")) {
            return null;
        }

        final String jwt = authToken.substring(7);

        final String userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var valid = jwtService
                    .isTokenValid(jwt, userDetails) && hasRole(userDetails, roleToHave);
            if (valid) {
                return jwtService.extractClaim(jwt, claims -> claims.get("userId", Integer.class)).toString();
            }
        }
        return null;
    }

    private boolean hasRole(UserDetails userDetails, String roleToHave) {
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority(roleToHave));
    }
}
