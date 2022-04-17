package com.assessment.tradestore.validations;

import com.assessment.tradestore.model.Trade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.assessment.tradestore.validations.ValidationMessage.LOWER_MATURITY_DATE;
import static com.assessment.tradestore.validations.ValidationMessage.LOWER_VERSION_ERROR;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public ValidationResult validateTradeTransmission(Trade existingTrade, Trade updatedTrade) {
        ValidationResult result = new ValidationResult();
        if (existingTrade.getVersion() > updatedTrade.getVersion()) {
            result.addErrors(LOWER_VERSION_ERROR);
        }
        if (updatedTrade.getMaturityDate().isBefore(LocalDate.now())) {
            result.addErrors(LOWER_MATURITY_DATE);
        }
        return result;
    }
}
