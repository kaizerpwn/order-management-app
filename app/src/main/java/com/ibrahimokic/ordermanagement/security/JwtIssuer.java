package com.ibrahimokic.ordermanagement.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ibrahimokic.ordermanagement.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtIssuer {
    private final JwtConfig config;
    public String issue(long userId, String email, String role) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.ofDays(1)))
                .withClaim("email", email)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(config.getSecretKey()));
    }
}
