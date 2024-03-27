package com.ibrahimokic.ordermanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/**")
                .sessionManagement(sessionManagementConfigurer
                        -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/users/login").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/api/product/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/product/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/product/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/product").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/address/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/address/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/address").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/orders/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .build();
    }
}
