package com.reliaquest.api.exception;

import java.util.Arrays;
import java.util.List;

public class NotFoundException extends Exception {
    private transient ErrorObject[] errors;

    public NotFoundException(ErrorObject... errors) {
        this.errors = errors;
    }

    public NotFoundException(List<String> errorsCodes) {
        if (errorsCodes != null && !errorsCodes.isEmpty()) {
            errors = new ErrorObject[errorsCodes.size()];
            int index = 0;
            for (String err : errorsCodes) {
                ErrorObject eo = new ErrorObject(err);
                errors[index++] = eo;
            }
        }
    }

    public ErrorObject[] getErrors() {
        return errors;
    }

    public void setErrors(ErrorObject[] errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return toString() + super.getMessage();
    }

    @Override
    public String toString() {
        return "NotFoundException{" + "errors=" + Arrays.toString(errors) + "} ";
    }
}
