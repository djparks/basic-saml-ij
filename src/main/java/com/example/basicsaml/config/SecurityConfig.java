package com.example.basicsaml.config;

import com.example.basicsaml.security.SecuredEndpointResponseFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Profile("!test & !basicauth")
@Order(2) // Apply after JwtSecurityConfig
public class SecurityConfig {

    private final SecuredEndpointResponseFilter securedEndpointResponseFilter;

    public SecurityConfig(SecuredEndpointResponseFilter securedEndpointResponseFilter) {
        this.securedEndpointResponseFilter = securedEndpointResponseFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // This is a basic configuration that can be customized based on requirements
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configure CORS
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/gate/**").permitAll()
                .anyRequest().authenticated()
            )
            .saml2Login(saml2 -> saml2
                .loginPage("/login")
            )
            .saml2Logout(saml2 -> saml2
                .logoutUrl("/logout")
            )
            // Add the SecuredEndpointResponseFilter to log response codes for /secured endpoint
            .addFilterBefore(securedEndpointResponseFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(java.util.Collections.singletonList("https://google.com")); // Allow only https://google.com
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // Allow all methods
        configuration.setAllowedHeaders(java.util.Collections.singletonList("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials since we're using a specific origin
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }
}
