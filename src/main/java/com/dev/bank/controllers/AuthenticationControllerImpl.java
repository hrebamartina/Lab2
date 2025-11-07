package com.dev.bank.controllers;

import com.dev.bank.controllers.client.AuthenticationController;
import com.dev.bank.models.request.AuthLoginRequest;
import com.dev.bank.models.request.AuthRegisterRequest;
import com.dev.bank.models.response.AuthLoginResponse;
import com.dev.bank.models.response.AuthRegisterResponse;
import com.dev.bank.services.client.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationControllerImpl implements AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationControllerImpl.class);

    @Autowired
    private AuthenticationService service;

    @Override
    public ResponseEntity<AuthLoginResponse> login(@Valid AuthLoginRequest request) {
        log.info("Received request [POST /auth/login] for: {}", request.getUsername());
        AuthLoginResponse response = service.login(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthRegisterResponse> register(@Valid AuthRegisterRequest request) {
        log.info("Received request [POST /auth/register] for: {}", request.getUsername());
        AuthRegisterResponse response = service.register(request);
        return ResponseEntity.ok(response);
    }
}
