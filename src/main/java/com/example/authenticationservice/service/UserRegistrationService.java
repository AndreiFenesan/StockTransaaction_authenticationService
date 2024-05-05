package com.example.authenticationservice.service;

import com.example.authenticationservice.controller.authentication.CredentialsRequestBody;
import com.example.authenticationservice.datasource.model.BasicUser;
import com.example.authenticationservice.datasource.repository.UserRepository;
import com.example.authenticationservice.exception.EmailAlreadyTakenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(CredentialsRequestBody credentialsRequestBody) {
        userRepository.findUserByEmail(credentialsRequestBody.getUserEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyTakenException("Email already taken");
                });


        var user = BasicUser.builder()
                .email(credentialsRequestBody.getUserEmail())
                .password(passwordEncoder.encode(credentialsRequestBody.getPassword()))
                .build();

        userRepository.save(user);
    }
}
