package com.ibrahimokic.ordermanagement.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("security.jwt")
public class JwtConfig {
    @Value("${security.jwt.secret}")
    private String secretKey;
}
