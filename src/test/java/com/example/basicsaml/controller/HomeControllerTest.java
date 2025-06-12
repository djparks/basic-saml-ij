package com.example.basicsaml.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.basicsaml.config.TestSecurityConfig;

@WebMvcTest(HomeController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomeEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Welcome to the SAML Application"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Public page"));
    }

    @Test
    public void testSecuredEndpoint() throws Exception {
        // In test mode with TestSecurityConfig, this endpoint should be accessible without authentication
        mockMvc.perform(MockMvcRequestBuilders.get("/secured"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("This is a secured page"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Authenticated"));
    }

    @Test
    public void testLoginEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Please log in"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Login page"));
    }
}