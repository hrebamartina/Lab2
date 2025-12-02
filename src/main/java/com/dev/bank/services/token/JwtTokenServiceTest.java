package com.dev.bank.services.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
public class JwtTokenServiceTest {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenServiceTest.class);

    private final String secretKey;
    private final long expirationTimeMs;
    private final String appName;
    private final Map<String, Date> tokenBlacklist = new ConcurrentHashMap<>();
    public JwtTokenServiceTest(@Value("${jwt.secret.key}") String secretKey,
                               @Value("${jwt.expiration.ms}") long expirationTimeMs,
                               @Value("${jwt.app.name}") String appName) {
        this.secretKey = secretKey;
        this.expirationTimeMs = expirationTimeMs;
        this.appName = appName;
        log.info("JwtTokenService ініціалізовано. Назва додатку: {}", appName);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, Map<String, Object> userMetadata) {

        Map<String, Object> claims = new HashMap<>(userMetadata);
        claims.put("app_name", appName);

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + expirationTimeMs);

        log.info("Генерація токену для: {}. Термін дії до: {}", username, expiryDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token) {
        try {
            if (tokenBlacklist.containsKey(token)) {
                log.warn("Токен інвалідовано.");
                return false;
            }
            return !isTokenExpired(token);

        } catch (Exception e) {
            log.error("Помилка валідації токену: {}", e.getMessage());
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Map<String, Object> extractUserMetadata(String token) {
        Claims claims = extractAllClaims(token);
        Map<String, Object> metadata = new HashMap<>(claims);
        metadata.remove(Claims.SUBJECT);
        metadata.remove(Claims.ISSUED_AT);
        metadata.remove(Claims.EXPIRATION);
        metadata.remove(Claims.AUDIENCE);
        metadata.remove("app_name");
        return metadata;
    }

    public void invalidateToken(String token) {
        if (token != null && !tokenBlacklist.containsKey(token)) {
            Date expiration = extractExpiration(token);
            tokenBlacklist.put(token, expiration);
            log.info("Токен успішно інвалідовано.");
        }
    }
}