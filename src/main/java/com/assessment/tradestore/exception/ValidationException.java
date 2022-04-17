package com.assessment.tradestore.exception;

import java.util.List;

public class ValidationException extends RuntimeException{

    private List<String> errors;

    public ValidationException(List<String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public List<String> getValidationError() {
        return errors;
    }
}
