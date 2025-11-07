package com.dev.bank.controllers.client;

import com.dev.bank.models.request.AuthLoginRequest;
import com.dev.bank.models.request.AuthRegisterRequest;
import com.dev.bank.models.response.AuthLoginResponse;
import com.dev.bank.models.response.AuthRegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
@RequestMapping("/auth") //http://localhost:8080/auth/
public interface AuthenticationController {

    @PostMapping("/login") //POST http://localhost:8080/auth/login
    ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request);

    @PostMapping("/register") //POST http://localhost:8080/auth/register
    ResponseEntity<AuthRegisterResponse> register(@Valid @RequestBody AuthRegisterRequest request);
}