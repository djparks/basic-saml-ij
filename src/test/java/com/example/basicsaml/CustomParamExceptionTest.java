package com.example.basicsaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CustomParamException} class.
 * These tests verify the correct behavior of the exception class
 * including parameter handling and problem structure.
 */
public class CustomParamExceptionTest {

    @Test
    public void testConstructorWithStringParams() {
        // Test with a single parameter
        CustomParamException exception = new CustomParamException("Error message", "param1");
        
        // Verify the exception properties
        ProblemDetail problemDetail = exception.getBody();
        assertEquals(ErrorConstants.PARAMETERIZED_TYPE, problemDetail.getType());
        assertEquals("Parameterized Exception", problemDetail.getTitle());
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        
        // Verify the parameters map structure
        Map<String, Object> parameters = exception.getParameters();
        assertNotNull(parameters);
        assertEquals("Error message", parameters.get("message"));
        
        // Verify the param map structure
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) parameters.get("param");
        assertNotNull(paramMap);
        assertEquals("param1", paramMap.get("param0"));
        
        System.out.println("[DEBUG_LOG] Constructor with single string parameter test passed!");
    }
    
    @Test
    public void testConstructorWithMultipleStringParams() {
        // Test with multiple parameters
        CustomParamException exception = new CustomParamException(
                "Multiple params error", "param1", "param2", "param3");
        
        // Verify the parameters map structure
        Map<String, Object> parameters = exception.getParameters();
        assertNotNull(parameters);
        
        // Verify the param map structure
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) parameters.get("param");
        assertNotNull(paramMap);
        assertEquals("param1", paramMap.get("param0"));
        assertEquals("param2", paramMap.get("param1"));
        assertEquals("param3", paramMap.get("param2"));
        assertEquals(3, paramMap.size());
        
        System.out.println("[DEBUG_LOG] Constructor with multiple string parameters test passed!");
    }
    
    @Test
    public void testConstructorWithEmptyStringParams() {
        // Test with empty string array
        CustomParamException exception = new CustomParamException("Empty params error");
        
        // Verify the parameters map structure
        Map<String, Object> parameters = exception.getParameters();
        assertNotNull(parameters);
        
        // Verify the param map structure
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) parameters.get("param");
        assertNotNull(paramMap);
        assertTrue(paramMap.isEmpty());
        
        System.out.println("[DEBUG_LOG] Constructor with empty string parameters test passed!");
    }
    
    @Test
    public void testConstructorWithNullStringParams() {
        // Test with null string array (should be handled gracefully)
        String[] nullParams = null;
        CustomParamException exception = new CustomParamException("Null params error", nullParams);
        
        // Verify the parameters map structure
        Map<String, Object> parameters = exception.getParameters();
        assertNotNull(parameters);
        
        // Verify the param map structure
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) parameters.get("param");
        assertNotNull(paramMap);
        assertTrue(paramMap.isEmpty());
        
        System.out.println("[DEBUG_LOG] Constructor with null string parameters test passed!");
    }
    
    @Test
    public void testConstructorWithMapParams() {
        // Create a parameter map
        Map<String, Object> inputParamMap = new HashMap<>();
        inputParamMap.put("key1", "value1");
        inputParamMap.put("key2", 123);
        inputParamMap.put("key3", true);
        
        // Create exception with the map
        CustomParamException exception = new CustomParamException(
                "Map params error", inputParamMap);
        
        // Verify the exception properties
        ProblemDetail problemDetail = exception.getBody();
        assertEquals(ErrorConstants.PARAMETERIZED_TYPE, problemDetail.getType());
        assertEquals("Parameterized Exception", problemDetail.getTitle());
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        
        // Verify the parameters map structure
        Map<String, Object> parameters = exception.getParameters();
        assertNotNull(parameters);
        assertEquals("Map params error", parameters.get("message"));
        
        // Verify the param map structure
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) parameters.get("param");
        assertNotNull(paramMap);
        assertEquals("value1", paramMap.get("key1"));
        assertEquals(123, paramMap.get("key2"));
        assertEquals(true, paramMap.get("key3"));
        
        System.out.println("[DEBUG_LOG] Constructor with map parameters test passed!");
    }
    
    @Test
    public void testConstructorWithEmptyMapParams() {
        // Test with empty map
        Map<String, Object> emptyMap = new HashMap<>();
        CustomParamException exception = new CustomParamException(
                "Empty map error", emptyMap);
        
        // Verify the parameters map structure
        Map<String, Object> parameters = exception.getParameters();
        assertNotNull(parameters);
        
        // Verify the param map structure
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = (Map<String, Object>) parameters.get("param");
        assertNotNull(paramMap);
        assertTrue(paramMap.isEmpty());
        
        System.out.println("[DEBUG_LOG] Constructor with empty map parameters test passed!");
    }
    
    @Test
    public void testExceptionInheritance() {
        // Verify the exception inherits from ErrorResponseException
        CustomParamException exception = new CustomParamException("Test message");
        assertTrue(exception instanceof ErrorResponseException);
        
        System.out.println("[DEBUG_LOG] Exception inheritance test passed!");
    }
    
    @Test
    public void testSerializationSupport() {
        // Verify the exception has a serialVersionUID
        CustomParamException exception = new CustomParamException("Test message");
        
        // This is a simple check that the class is serializable
        // A more thorough test would actually serialize and deserialize the object
        assertTrue(exception instanceof java.io.Serializable);
        
        System.out.println("[DEBUG_LOG] Serialization support test passed!");
    }
    
    @Test
    public void testJsonRepresentation() throws Exception {
        // Create a parameter map with known values
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("key1", "value1");
        paramMap.put("key2", 123);
        paramMap.put("key3", true);
        
        // Create exception with the map
        CustomParamException exception = new CustomParamException("JSON test error", paramMap);
        
        // Get the ProblemDetail from the exception
        ProblemDetail problemDetail = exception.getBody();
        
        // Create an ObjectMapper for JSON serialization
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Convert the ProblemDetail to JSON
        String actualJson = objectMapper.writeValueAsString(problemDetail);
        
        // Define the expected JSON structure using a text block
        String expectedJson = """
        {
            "type":"https://example.com/parameterized-error",
            "title":"Parameterized Exception",
            "status":400,
            "detail":null,
            "instance":null,
            "properties":{
                "message":"JSON test error",
                "param":{
                    "key1":"value1",
                    "key2":123,
                    "key3":true
                }
            }
        }
        """;
        
        // Use JSONAssert for comparison
        JSONAssert.assertEquals(expectedJson, actualJson, true);
        
        System.out.println("[DEBUG_LOG] JSON representation test passed!");
    }
}