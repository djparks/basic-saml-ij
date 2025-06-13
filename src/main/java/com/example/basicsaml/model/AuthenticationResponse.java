package com.example.basicsaml.model;

/**
 * Model class for authentication responses containing JWT token
 */
public class AuthenticationResponse {
    private final String jwt;
    private final String username;
    private final long expiresIn;

    public AuthenticationResponse(String jwt, String username, long expiresIn) {
        this.jwt = jwt;
        this.username = username;
        this.expiresIn = expiresIn;
    }

    public String getJwt() {
        return jwt;
    }

    public String getUsername() {
        return username;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}