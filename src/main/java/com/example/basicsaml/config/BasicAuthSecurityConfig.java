package com.example.basicsaml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration for when the "basicauth" profile is active.
 * This configuration does not include SAML authentication.
 */
@Configuration
@EnableWebSecurity
@Profile("basicauth")
@Order(2) // Apply after JwtSecurityConfig
public class BasicAuthSecurityConfig {

    @Bean
    public SecurityFilterChain basicAuthSecurityFilterChain(HttpSecurity http) throws Exception {
        // Basic security configuration without SAML
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configure CORS
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/gate/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {}); // Use HTTP Basic authentication instead of SAML

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(java.util.Collections.singletonList("*")); // Allow all origins
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // Allow all methods
        configuration.setAllowedHeaders(java.util.Collections.singletonList("*")); // Allow all headers
        configuration.setAllowCredentials(false); // Don't allow credentials when using "*" for origins
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }
}
