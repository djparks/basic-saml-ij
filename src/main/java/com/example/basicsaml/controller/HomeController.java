package com.example.basicsaml.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the SAML Application");
        response.put("status", "Public page");
        return response;
    }

    @GetMapping("/secured")
    @ResponseBody
    public Map<String, Object> secured(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a secured page");
        response.put("status", "Authenticated");
        
        if (principal != null) {
            response.put("username", principal.getName());
            response.put("attributes", principal.getAttributes());
        }
        
        return response;
    }

    @GetMapping("/login")
    @ResponseBody
    public Map<String, String> login() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Please log in");
        response.put("status", "Login page");
        return response;
    }
}