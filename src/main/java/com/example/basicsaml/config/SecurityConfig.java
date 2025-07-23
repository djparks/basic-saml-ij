package com.example.basicsaml.config;

import com.example.basicsaml.security.SecuredEndpointResponseFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
}
