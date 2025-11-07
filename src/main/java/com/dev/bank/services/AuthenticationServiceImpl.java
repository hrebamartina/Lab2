package com.dev.bank.services;

import com.dev.bank.models.request.AuthLoginRequest;
import com.dev.bank.models.request.AuthRegisterRequest;
import com.dev.bank.models.response.AuthLoginResponse;
import com.dev.bank.models.response.AuthRegisterResponse;
import com.dev.bank.services.client.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public AuthLoginResponse login(AuthLoginRequest request) {

        log.info("Trying to log in user: {}", request.getUsername());

        AuthLoginResponse response = new AuthLoginResponse();
        response.setSuccess(true);
        response.setMessage("User logged in successfully");
        response.setToken("dummy-jwt-token-for-" + request.getUsername());

        log.info("User {} logged in successfully.", request.getUsername());
        return response;
    }

    @Override
    public AuthRegisterResponse register(AuthRegisterRequest request) {
        log.info("Trying to register user: {}", request.getUsername());
        log.info("Data: Email={}, Birthday={}, Phone={}",
                request.getEmail(), request.getBirthday(), request.getPhoneNumber());

        AuthRegisterResponse response = new AuthRegisterResponse();
        response.setSuccess(true);
        response.setMessage("User registered successfully");

        log.warn("User {} registered (SIMULATION, database not connected).", request.getUsername());
        return response;
    }
}
