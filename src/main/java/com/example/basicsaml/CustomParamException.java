package com.example.basicsaml;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom exception for parameterized errors using Spring Boot 3 RFC 7807 implementation.
 * This exception provides a way to include additional parameters in the error response.
 */
public class CustomParamException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;
    private static final String PARAM = "param";

    /**
     * Creates a new CustomParamException with a message and optional string parameters.
     *
     * @param message the error message
     * @param params optional string parameters
     */
    public CustomParamException(String message, String... params) {
        this(message, toParamMap(params));
    }

    /**
     * Creates a new CustomParamException with a message and a parameter map.
     *
     * @param message the error message
     * @param paramMap the parameter map
     */
    public CustomParamException(String message, Map<String, Object> paramMap) {
        super(HttpStatus.BAD_REQUEST, createProblemDetail(message, paramMap), null);
    }

    /**
     * Creates a ProblemDetail with the given message and parameters.
     *
     * @param message the error message
     * @param paramMap the parameter map
     * @return the created ProblemDetail
     */
    private static ProblemDetail createProblemDetail(String message, Map<String, Object> paramMap) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(ErrorConstants.PARAMETERIZED_TYPE);
        problemDetail.setTitle("Parameterized Exception");
        
        // Add parameters to the problem detail
        problemDetail.setProperty("message", message);
        problemDetail.setProperty("param", paramMap);
        
        return problemDetail;
    }

    /**
     * Converts a string array to a parameter map.
     *
     * @param params the string array
     * @return the parameter map
     */
    private static Map<String, Object> toParamMap(String[] params) {
        Map<String, Object> paramMap = new HashMap<>();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                paramMap.put(PARAM + i, params[i]);
            }
        }
        return paramMap;
    }

    /**
     * Gets the parameters from the problem detail.
     *
     * @return the parameters map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        ProblemDetail problemDetail = getBody();
        Map<String, Object> properties = problemDetail.getProperties();
        
        if (properties != null) {
            if (properties.containsKey("message")) {
                parameters.put("message", properties.get("message"));
            }
            if (properties.containsKey("param")) {
                parameters.put("param", properties.get("param"));
            }
        }
        
        return parameters;
    }
}