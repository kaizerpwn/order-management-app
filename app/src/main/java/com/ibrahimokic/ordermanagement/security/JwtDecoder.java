package com.ibrahimokic.ordermanagement.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ibrahimokic.ordermanagement.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {
    private final JwtConfig jwtConfig;
    public DecodedJWT decode(String token) {
        return JWT
                .require(Algorithm.HMAC256(jwtConfig.getSecretKey()))
                .build()
                .verify(token);
    }
}
