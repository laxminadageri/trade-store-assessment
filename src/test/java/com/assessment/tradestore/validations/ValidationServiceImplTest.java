package com.assessment.tradestore.validations;

import com.assessment.tradestore.model.Trade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.assessment.tradestore.utils.TestUtil.existingTrade;
import static com.assessment.tradestore.utils.TestUtil.updatedTrade;
import static com.assessment.tradestore.validations.ValidationMessage.LOWER_MATURITY_DATE;
import static com.assessment.tradestore.validations.ValidationMessage.LOWER_VERSION_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceImplTest {

    @InjectMocks
    private ValidationServiceImpl validationService;


    @Test
    public void validate_lowerMaturityDate() {
        Trade existing = existingTrade("T1", 1);
        Trade updated = updatedTrade("T1", 1);
        updated.setMaturityDate(LocalDate.now().minusDays(1)); // maturity date is updated explicitly

        ValidationResult result = validationService.validateTradeTransmission(existing, updated);

        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrors().size());
        assertEquals(LOWER_MATURITY_DATE, result.getErrors().get(0));
    }

    @Test
    public void validate_lowerVersion() {
        Trade existing = existingTrade("T1", 2);
        Trade updated = updatedTrade("T1", 1); //lower version passed than existing trade version

        ValidationResult result = validationService.validateTradeTransmission(existing, updated);

        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrors().size());
        assertEquals(LOWER_VERSION_ERROR, result.getErrors().get(0));
    }

    @Test
    public void validate_lowerVersionAndLowerMaturityDate() {
        Trade existing = existingTrade("T1", 2);
        Trade updated = updatedTrade("T1", 1);
        updated.setMaturityDate(LocalDate.now().minusDays(1));

        ValidationResult result = validationService.validateTradeTransmission(existing, updated);

        List<String> errors = List.of(LOWER_VERSION_ERROR, LOWER_MATURITY_DATE);
        assertTrue(result.hasErrors());
        assertEquals(2, result.getErrors().size());
        assertTrue(result.getErrors().containsAll(errors));
    }
}
