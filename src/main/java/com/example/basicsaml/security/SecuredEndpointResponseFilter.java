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
                
                // Extract all headers and concatenate them into a single string
                StringBuilder headersString = new StringBuilder();
                java.util.Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    String headerValue = request.getHeader(headerName);
                    headersString.append(headerName).append("=").append(headerValue);
                    if (headerNames.hasMoreElements()) {
                        headersString.append(", ");
                    }
                }
                
                // Log the status code and headers
                logger.info("Request to /secured endpoint returned status code: {}. Headers: {}", 
                            statusCode, headersString.toString());
                
                // Copy content from the wrapped response to the original response
                responseWrapper.copyBodyToResponse();
            }
        } else {
            // For other endpoints, continue the filter chain without additional processing
            filterChain.doFilter(request, response);
        }
    }
}