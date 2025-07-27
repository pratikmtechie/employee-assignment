package com.reliaquest.api.exception;

import java.util.Arrays;
import java.util.Locale;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ControllerAdvice
public class ExceptionController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorObject[] handleServiceException(ServiceException ce) {
        ErrorObject[] errors = ce.getErrors();
        for (ErrorObject eo : errors) {
            if (eo.getErrorMessage() == null || eo.getErrorMessage().isEmpty()) {
                try {
                    eo.setErrorMessage(messageSource.getMessage(eo.getErrorCode(), eo.getArguments(), Locale.US));
                } catch (NoSuchMessageException e) {
                    eo.setErrorMessage("No message available for code: " + eo.getErrorCode());
                }
            }
        }
        log.warn("Api called failed with " + Arrays.toString(errors));
        return errors;
    }

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    @ResponseBody
    public ErrorObject[] handleRateLimitException(RateLimitException ce) {
        ErrorObject[] errors = ce.getErrors();
        for (ErrorObject eo : errors) {
            if (eo.getErrorMessage() == null || eo.getErrorMessage().isEmpty()) {
                try {
                    eo.setErrorMessage(messageSource.getMessage(eo.getErrorCode(), eo.getArguments(), Locale.US));
                } catch (NoSuchMessageException e) {
                    eo.setErrorMessage("No message available for code: " + eo.getErrorCode());
                }
            }
        }
        log.warn("Api called failed with " + Arrays.toString(errors));
        return errors;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorObject[] handleRateLimitException(NotFoundException ce) {
        ErrorObject[] errors = ce.getErrors();
        for (ErrorObject eo : errors) {
            if (eo.getErrorMessage() == null || eo.getErrorMessage().isEmpty()) {
                try {
                    eo.setErrorMessage(messageSource.getMessage(eo.getErrorCode(), eo.getArguments(), Locale.US));
                } catch (NoSuchMessageException e) {
                    eo.setErrorMessage("No message available for code: " + eo.getErrorCode());
                }
            }
        }
        log.warn("Api called failed with " + Arrays.toString(errors));
        return errors;
    }
}
