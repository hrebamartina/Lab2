package com.dev.bank.security;

import com.dev.bank.services.token.JwtTokenServiceTest;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtTokenServiceTest tokenService;

    public JwtAuthFilter(JwtTokenServiceTest tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = tokenService.extractUsername(token);
            } catch (ExpiredJwtException e) {
                log.warn("Токен прострочений для URI: {}", request.getRequestURI());
            } catch (SignatureException e) {
                log.error("Неправильний підпис токену для URI: {}", request.getRequestURI());
            } catch (Exception e) {
                log.error("Помилка обробки токену: {}", e.getMessage());
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (token != null && tokenService.isTokenValid(token)) {

                Map<String, Object> metadata = tokenService.extractUserMetadata(token);


                request.setAttribute("username", username);
                request.setAttribute("userMetadata", metadata);



                UserDetails userDetails = new User(username, "", Collections.emptyList());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Автентифікація успішна для користувача: {}", username);
            } else {
                log.warn("Токен невалідний або в чорному списку для: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}