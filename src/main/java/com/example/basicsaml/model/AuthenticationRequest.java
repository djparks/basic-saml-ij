package com.example.basicsaml.model;

/**
 * Model class for authentication requests containing username and password
 */
public class AuthenticationRequest {
    private String username;
    private String password;

    // Default constructor for JSON deserialization
    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}