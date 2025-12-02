package com.dev.bank.services;

import com.dev.bank.models.request.AuthLoginRequest;
import com.dev.bank.models.request.AuthRegisterRequest;
import com.dev.bank.models.response.AuthLoginResponse;
import com.dev.bank.models.response.AuthRegisterResponse;
import com.dev.bank.services.client.AuthenticationService;
import com.dev.bank.services.token.JwtTokenServiceTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private JwtTokenServiceTest jwtTokenService;

    @Override
    public AuthLoginResponse login(AuthLoginRequest request) {

        log.info("Спроба входу користувача: {}", request.getUsername());

        Map<String, Object> userMetadata = Map.of(
                "user_id", "user_" + request.getUsername(),
                "role", "CLIENT"
        );
        String token = jwtTokenService.generateToken(
                request.getUsername(),
                userMetadata
        );

        AuthLoginResponse response = new AuthLoginResponse();
        response.setSuccess(true);
        response.setMessage("Користувач успішно увійшов. Токен надано.");
        response.setToken(token);

        log.info("Користувач {} успішно увійшов. Токен згенеровано.", request.getUsername());
        return response;
    }

    @Override
    public AuthRegisterResponse register(AuthRegisterRequest request) {
        log.info("Спроба реєстрації користувача: {}", request.getUsername());
        log.info("Дані: Email={}, BirthDate={}, Phone={}",
                request.getEmail(), request.getBirthday(), request.getPhoneNumber());

        Map<String, Object> userMetadata = Map.of(
                "user_id", "user_" + request.getUsername(),
                "role", "CLIENT"
        );

        String token = jwtTokenService.generateToken(
                request.getUsername(),
                userMetadata
        );

        AuthRegisterResponse response = new AuthRegisterResponse();
        response.setSuccess(true);
        response.setMessage("Користувач успішно зареєстрований. Токен надано.");
        response.setToken(token);

        log.warn("Користувач {} зареєстрований (СИМУЛЯЦІЯ). Токен згенеровано.", request.getUsername());
        return response;
    }
}