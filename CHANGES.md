# Changes for Secured Endpoint Response Logging

## Overview
This document describes the changes made to implement a filter that logs the HTTP status code when the `/secured` endpoint is called.

## Implementation Details

### 1. Created SecuredEndpointResponseFilter

Created a new filter class in the security package that logs the HTTP status code when the `/secured` endpoint is called:

```java
// File: src/main/java/com/example/basicsaml/security/SecuredEndpointResponseFilter.java
package com.example.basicsaml.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * Filter that logs the HTTP status code when the /secured endpoint is called.
 */
@Component
public class SecuredEndpointResponseFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecuredEndpointResponseFilter.class);
    private static final String SECURED_ENDPOINT = "/secured";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Check if this is a request to the /secured endpoint
        if (request.getRequestURI().equals(SECURED_ENDPOINT)) {
            // Wrap the response to capture the status code
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            
            try {
                // Continue the filter chain with the wrapped response
                filterChain.doFilter(request, responseWrapper);
            } finally {
                // Log the status code after the request has been processed
                int statusCode = responseWrapper.getStatus();
                logger.info("Request to /secured endpoint returned status code: {}", statusCode);
                
                // Copy content from the wrapped response to the original response
                responseWrapper.copyBodyToResponse();
            }
        } else {
            // For other endpoints, continue the filter chain without additional processing
            filterChain.doFilter(request, response);
        }
    }
}
```

### 2. Updated SecurityConfig

Modified the SecurityConfig class to include the SecuredEndpointResponseFilter in the security filter chain:

```java
// File: src/main/java/com/example/basicsaml/config/SecurityConfig.java
// Added imports
import com.example.basicsaml.security.SecuredEndpointResponseFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Added field and constructor
private final SecuredEndpointResponseFilter securedEndpointResponseFilter;

public SecurityConfig(SecuredEndpointResponseFilter securedEndpointResponseFilter) {
    this.securedEndpointResponseFilter = securedEndpointResponseFilter;
}

// Added filter to the security filter chain
http
    .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/").permitAll()
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
```

## How It Works

1. The SecuredEndpointResponseFilter is registered as a Spring component and injected into the SecurityConfig.
2. The filter is added to the security filter chain before the UsernamePasswordAuthenticationFilter.
3. When a request is made to the `/secured` endpoint:
   - The filter wraps the response in a ContentCachingResponseWrapper to capture the status code
   - After the request is processed, the filter logs the status code
   - The content from the wrapped response is copied to the original response
4. For all other endpoints, the filter simply passes the request and response to the next filter in the chain.

## Logging Output

When the `/secured` endpoint is called, the filter will log a message like this:

```
Request to /secured endpoint returned status code: 200
```

The status code will vary depending on the outcome of the request (e.g., 200 for success, 401 for unauthorized, etc.).