package com.reliaquest.api.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class NotFoundExceptionTest {

    @Test
    void testConstructorWithErrorObjects() {
        ErrorObject eo1 = new ErrorObject("ERR001", "Error 1");
        ErrorObject eo2 = new ErrorObject("ERR002", "Error 2");

        NotFoundException ex = new NotFoundException(eo1, eo2);
        assertNotNull(ex.getErrors());
        assertEquals(2, ex.getErrors().length);
        assertEquals("ERR001", ex.getErrors()[0].getErrorCode());
        assertEquals("ERR002", ex.getErrors()[1].getErrorCode());
    }

    @Test
    void testConstructorWithErrorCodesList() {
        List<String> errorCodes = List.of("ERR003", "ERR004");
        NotFoundException ex = new NotFoundException(errorCodes);

        assertNotNull(ex.getErrors());
        assertEquals(2, ex.getErrors().length);
        assertEquals("ERR003", ex.getErrors()[0].getErrorCode());
        assertEquals("ERR004", ex.getErrors()[1].getErrorCode());
    }

    @Test
    void testConstructorWithEmptyErrorCodesList() {
        NotFoundException ex = new NotFoundException(List.of());

        assertNull(ex.getErrors());
    }

    @Test
    void testSetAndGetErrors() {
        NotFoundException ex = new NotFoundException();
        ErrorObject[] errors = {new ErrorObject("ERR005")};

        ex.setErrors(errors);

        assertArrayEquals(errors, ex.getErrors());
    }

    @Test
    void testGetMessageContainsErrors() {
        ErrorObject eo = new ErrorObject("ERR006", "Error message");
        NotFoundException ex = new NotFoundException(eo);

        String msg = ex.getMessage();

        assertTrue(msg.contains("ERR006"));
        assertTrue(msg.contains("NotFoundException"));
    }

    @Test
    void testToStringContainsErrors() {
        ErrorObject eo = new ErrorObject("ERR007", "Error message");
        NotFoundException ex = new NotFoundException(eo);

        String str = ex.toString();

        assertTrue(str.contains("ERR007"));
        assertTrue(str.contains("NotFoundException"));
    }
}
