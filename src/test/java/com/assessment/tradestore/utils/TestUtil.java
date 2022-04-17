package com.assessment.tradestore.utils;

import com.assessment.tradestore.model.Trade;
import com.assessment.tradestore.validations.ValidationResult;

import java.time.LocalDate;

public class TestUtil {

    public static ValidationResult mockValidationResult(String error) {
        ValidationResult result = new ValidationResult();
        result.addErrors(error);
        return result;
    }

    public static Trade updatedTrade(String tradeId, int version) {
        return new Trade(tradeId, version, "CP-2", "B2",
                LocalDate.now().plusDays(10), LocalDate.now(), 'N');
    }

    public static Trade existingTrade(String tradeId, int version) {
        return new Trade(tradeId, version, "CP-1", "B1",
                LocalDate.now().plusDays(10), LocalDate.now(), 'N');
    }
}
