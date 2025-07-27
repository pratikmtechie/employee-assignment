package com.reliaquest.api.exception;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class ErrorObjectTest {

    @Test
    void testAllArgsConstructor() {
        ErrorObject error = new ErrorObject("ERR001", "Invalid Input");

        assertEquals("ERR001", error.getErrorCode());
        assertEquals("Invalid Input", error.getErrorMessage());
        assertNull(error.getArguments());
    }

    @Test
    void testErrorCodeOnlyConstructor() {
        ErrorObject error = new ErrorObject("ERR002");

        assertEquals("ERR002", error.getErrorCode());
        assertNull(error.getErrorMessage());
        assertNull(error.getArguments());
    }

    @Test
    void testErrorCodeAndArgumentsConstructor() {
        Object[] args = {"field1", "field2"};
        ErrorObject error = new ErrorObject("ERR003", args);

        assertEquals("ERR003", error.getErrorCode());
        assertArrayEquals(args, error.getArguments());
        assertNull(error.getErrorMessage());
    }

    @Test
    void testSettersAndGetters() {
        ErrorObject error = new ErrorObject();
        error.setErrorCode("ERR004");
        error.setErrorMessage("Something went wrong");
        error.setArguments("arg1", 42);

        assertEquals("ERR004", error.getErrorCode());
        assertEquals("Something went wrong", error.getErrorMessage());
        assertArrayEquals(new Object[] {"arg1", 42}, error.getArguments());
    }

    @Test
    void testToString() {
        ErrorObject error = new ErrorObject();
        error.setErrorCode("ERR005");
        error.setErrorMessage("Test Error");
        error.setArguments("x", "y");

        String str = error.toString();

        assertTrue(str.contains("ERR005"));
        assertTrue(str.contains("Test Error"));
        assertTrue(str.contains("x"));
        assertTrue(str.contains("y"));
    }

    @Test
    void testJsonSerializationIgnoresArguments() throws Exception {
        ErrorObject error = new ErrorObject("ERR006", "Some error");
        error.setArguments("secret", 123);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(error);

        assertTrue(json.contains("ERR006"));
        assertTrue(json.contains("Some error"));
        assertFalse(json.contains("secret"));
        assertFalse(json.contains("123"));
        assertFalse(json.contains("arguments"));
    }
}
