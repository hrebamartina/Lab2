package com.dev.bank.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret.key=TestSecretKeyForBankApplicationDevelopment",
        "jwt.expiration.ms=3600000",
        "jwt.app.name=bank.BankApplication"
})
public class BankApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true, "Контекст програми має завантажитися.");
    }
}