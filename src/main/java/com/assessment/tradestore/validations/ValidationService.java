package com.assessment.tradestore.validations;

import com.assessment.tradestore.model.Trade;

public interface ValidationService {
    ValidationResult validateTradeTransmission(Trade existingTrade, Trade updatedTrade);
}
