package com.example.basicsaml.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Example test class to demonstrate testing in the project.
 */
public class StringUtilsTest {

    /**
     * A simple utility class for string operations.
     */
    static class StringUtils {
        /**
         * Reverses a string.
         * 
         * @param input The string to reverse
         * @return The reversed string
         */
        public static String reverse(String input) {
            if (input == null) {
                return null;
            }
            return new StringBuilder(input).reverse().toString();
        }
    }

    @Test
    public void testReverse() {
        // Test normal string reversal
        assertEquals("cba", StringUtils.reverse("abc"));
        
        // Test empty string
        assertEquals("", StringUtils.reverse(""));
        
        // Test null input
        assertNull(StringUtils.reverse(null));
        
        // Test palindrome
        assertEquals("radar", StringUtils.reverse("radar"));
        
        System.out.println("[DEBUG_LOG] All string reverse tests passed successfully!");
    }
}