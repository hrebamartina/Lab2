package com.dev.bank.controllers.client;

import com.dev.bank.models.request.AuthLoginRequest;
import com.dev.bank.models.request.AuthRegisterRequest;
import com.dev.bank.models.response.AuthLoginResponse;
import com.dev.bank.models.response.AuthRegisterResponse;
import com.dev.bank.services.client.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        AuthLoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        AuthRegisterResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }
}