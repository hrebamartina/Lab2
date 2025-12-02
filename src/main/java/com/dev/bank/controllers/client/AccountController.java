package com.dev.bank.controllers.client;

import com.dev.bank.annotations.Secured;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(
            @RequestAttribute("username") String username,
            @RequestAttribute("userMetadata") Map<String, Object> userMetadata) {

        String userId = userMetadata.getOrDefault("user_id", "N/A").toString();
        String role = userMetadata.getOrDefault("role", "GUEST").toString();

        String response = String.format("Access granted! Welcome, %s. " +
                        "Твій ID: %s, а Роль: %s. Ти використовуєш сучасну JWT-автентифікацію.",
                username, userId, role);

        return ResponseEntity.ok(response);
    }
}