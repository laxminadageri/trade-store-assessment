package com.assessment.tradestore.validations;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private List<String> validationErrors;

    public void addErrors(String error) {
        if(validationErrors == null) {
            validationErrors = new ArrayList<>();
        }
        validationErrors.add(error);
    }

    public List<String> getErrors() {
        return validationErrors;
    }

    public boolean hasErrors() {
        return validationErrors != null && !validationErrors.isEmpty();
    }
}
