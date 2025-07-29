package com.example.basicsaml;

import org.junit.jupiter.api.Test;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CustomParameterizedException} class.
 * These tests verify the correct behavior of the exception class
 * including parameter handling and problem structure.
 */
public class CustomParameterizedExceptionTest {

    @Test
    public void testConstructorWithStringParams() {
        // Test with a single parameter
        CustomParameterizedException exception = new CustomParameterizedException("Error message", "param1");
        
        // Verify the exception properties
        assertEquals(ErrorConstants.PARAMETERIZED_TYPE, exception.getType());
        assertEquals("Parameterized Exception", exception.getTitle());
        assertEquals(Status.BAD_REQUEST, exception.getStatus());
        
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
        CustomParameterizedException exception = new CustomParameterizedException(
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
        CustomParameterizedException exception = new CustomParameterizedException("Empty params error");
        
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
        CustomParameterizedException exception = new CustomParameterizedException("Null params error", nullParams);
        
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
        CustomParameterizedException exception = new CustomParameterizedException(
                "Map params error", inputParamMap);
        
        // Verify the exception properties
        assertEquals(ErrorConstants.PARAMETERIZED_TYPE, exception.getType());
        assertEquals("Parameterized Exception", exception.getTitle());
        assertEquals(Status.BAD_REQUEST, exception.getStatus());
        
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
        CustomParameterizedException exception = new CustomParameterizedException(
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
        // Verify the exception inherits from AbstractThrowableProblem
        CustomParameterizedException exception = new CustomParameterizedException("Test message");
        assertTrue(exception instanceof AbstractThrowableProblem);
        
        System.out.println("[DEBUG_LOG] Exception inheritance test passed!");
    }
    
    @Test
    public void testSerializationSupport() {
        // Verify the exception has a serialVersionUID
        CustomParameterizedException exception = new CustomParameterizedException("Test message");
        
        // This is a simple check that the class is serializable
        // A more thorough test would actually serialize and deserialize the object
        assertTrue(exception instanceof java.io.Serializable);
        
        System.out.println("[DEBUG_LOG] Serialization support test passed!");
    }
}