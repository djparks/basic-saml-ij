package com.example.basicsaml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for when the "basicauth" profile is active.
 * This configuration does not include SAML authentication.
 */
@Configuration
@EnableWebSecurity
@Profile("basicauth")
public class BasicAuthSecurityConfig {

    @Bean
    public SecurityFilterChain basicAuthSecurityFilterChain(HttpSecurity http) throws Exception {
        // Basic security configuration without SAML
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {}); // Use HTTP Basic authentication instead of SAML

        return http.build();
    }
}
