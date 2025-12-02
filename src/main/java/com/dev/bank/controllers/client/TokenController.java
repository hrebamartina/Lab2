package com.dev.bank.controllers.client;

import com.dev.bank.annotations.Secured;
import com.dev.bank.models.response.BaseResponse;
import com.dev.bank.services.token.JwtTokenServiceTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final JwtTokenServiceTest jwtTokenService;

    public TokenController(JwtTokenServiceTest jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }
    @Secured
    @PostMapping("/invalidate")
    public ResponseEntity<BaseResponse> invalidateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtTokenService.invalidateToken(token);
        }

        BaseResponse response = new BaseResponse();
        response.setSuccess(true);
        response.setMessage("Токен успішно інвалідовано (логаут).");
        return ResponseEntity.ok(response);
    }
    @Secured
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse> refreshToken(
            @RequestAttribute("username") String username,
            @RequestAttribute("userMetadata") Map<String, Object> userMetadata) {

        String newToken = jwtTokenService.generateToken(username, userMetadata);

        BaseResponse response = new BaseResponse();
        response.setSuccess(true);
        response.setMessage("Токен успішно оновлено.");
        response.setMessage(response.getMessage() + " Новий токен: " + newToken);

        return ResponseEntity.ok(response);
    }
}