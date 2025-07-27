package com.reliaquest.api.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorObject {
    private String errorCode;
    private String errorMessage;

    @JsonIgnore
    private Object[] arguments;

    public ErrorObject(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorObject(String errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorObject() {}

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object... arguments) {
        this.arguments = arguments;
    }

    public ErrorObject(String errorCode, Object[] arguments) {
        this.errorCode = errorCode;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "ErrorObject{" + "errorCode='"
                + errorCode + '\'' + ", errorMessage='"
                + errorMessage + '\'' + ", arguments="
                + Arrays.toString(arguments) + '}';
    }
}
