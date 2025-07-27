package com.reliaquest.api.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

class ExceptionControllerTest {

    @InjectMocks
    private ExceptionController exceptionController;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ErrorObject[] getErrorWithMissingMessage() {
        ErrorObject error = new ErrorObject("ERR001");
        error.setArguments("arg1", "arg2");
        return new ErrorObject[] {error};
    }

    @Test
    void testHandleServiceException_withResolvableMessage() {
        ErrorObject[] errors = getErrorWithMissingMessage();
        when(messageSource.getMessage("ERR001", errors[0].getArguments(), Locale.US))
                .thenReturn("Resolved message for ERR001");

        ServiceException exception = new ServiceException(errors);

        ErrorObject[] result = exceptionController.handleServiceException(exception);

        assertEquals("Resolved message for ERR001", result[0].getErrorMessage());
        verify(messageSource).getMessage("ERR001", errors[0].getArguments(), Locale.US);
    }

    @Test
    void testHandleServiceException_withMissingMessage() {
        ErrorObject[] errors = getErrorWithMissingMessage();
        when(messageSource.getMessage("ERR001", errors[0].getArguments(), Locale.US))
                .thenThrow(new NoSuchMessageException("ERR001"));

        ServiceException exception = new ServiceException(errors);

        ErrorObject[] result = exceptionController.handleServiceException(exception);

        assertEquals("No message available for code: ERR001", result[0].getErrorMessage());
    }

    @Test
    void testHandleRateLimitException_withResolvableMessage() {
        ErrorObject[] errors = getErrorWithMissingMessage();
        when(messageSource.getMessage("ERR001", errors[0].getArguments(), Locale.US))
                .thenReturn("Rate limit exceeded");

        RateLimitException exception = new RateLimitException(errors);

        ErrorObject[] result = exceptionController.handleRateLimitException(exception);

        assertEquals("Rate limit exceeded", result[0].getErrorMessage());
    }

    @Test
    void testHandleNotFoundException_withMissingMessage() {
        ErrorObject[] errors = getErrorWithMissingMessage();
        when(messageSource.getMessage("ERR001", errors[0].getArguments(), Locale.US))
                .thenThrow(new NoSuchMessageException("ERR001"));

        NotFoundException exception = new NotFoundException(errors);

        ErrorObject[] result = exceptionController.handleRateLimitException(exception);

        assertEquals("No message available for code: ERR001", result[0].getErrorMessage());
    }
}
