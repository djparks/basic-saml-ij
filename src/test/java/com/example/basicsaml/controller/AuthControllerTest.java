package com.example.basicsaml.controller;

import com.example.basicsaml.model.AuthenticationRequest;
import com.example.basicsaml.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.basicsaml.config.TestSecurityConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateAuthenticationToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("user", "password");

        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.expiresIn").exists());
    }

    @Test
    public void testSecuredEndpoint() throws Exception {
        // Create a test user
        UserDetails userDetails = new User("testuser", "", new ArrayList<>());

        // Generate a token for the test user
        String token = jwtUtil.generateToken(userDetails);

        // Test accessing the secured endpoint with the token
        mockMvc.perform(get("/api/auth/secured")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a secured endpoint accessible with JWT"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testSecuredEndpointWithoutToken() throws Exception {
        // Test accessing the secured endpoint without a token
        // Spring Security returns 403 Forbidden when authentication is required but not provided
        mockMvc.perform(get("/api/auth/secured"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testSecuredEndpointWithInvalidToken() throws Exception {
        // Test accessing the secured endpoint with an invalid token
        // Spring Security returns 403 Forbidden when an invalid token is provided
        mockMvc.perform(get("/api/auth/secured")
                .header("Authorization", "Bearer invalidtoken"))
                .andExpect(status().isForbidden());
    }
}
