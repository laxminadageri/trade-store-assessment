package com.assessment.tradestore.service;

import com.assessment.tradestore.exception.ValidationException;
import com.assessment.tradestore.model.Trade;
import com.assessment.tradestore.repository.TradeRepository;
import com.assessment.tradestore.validations.ValidationResult;
import com.assessment.tradestore.validations.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.assessment.tradestore.utils.TestUtil.*;
import static com.assessment.tradestore.validations.ValidationMessage.LOWER_VERSION_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeServiceImplTest {

    @Mock
    private ValidationService validationService;
    @Mock
    private TradeRepository tradeRepository;
    @InjectMocks
    private TradeServiceImpl tradeService;

    @Test
    public void test_tradeTransmission_success() {
        Trade existing = existingTrade("T1", 1); // get existing trade
        Trade updated = updatedTrade("T1", 1); //get incoming trade (to update the existing trade)

        when(tradeRepository.getLatestTradeById(updated.getTradeId())).thenReturn(existing);
        when(validationService.validateTradeTransmission(existing, updated)).thenReturn(new ValidationResult());

        tradeService.tradeTransmission(updated);

        verify(tradeRepository, times(1)).update(existing, updated);
    }

    @Test
    public void test_tradeTransmission_throws_validationEx() {
        Trade existing = existingTrade("T2", 2);
        Trade updated = updatedTrade("T2", 1); // same trade ID but lower version is passed

        ValidationResult result = mockValidationResult(LOWER_VERSION_ERROR);

        when(tradeRepository.getLatestTradeById(updated.getTradeId())).thenReturn(existing);
        when(validationService.validateTradeTransmission(existing, updated)).thenReturn(result);

        try {
            tradeService.tradeTransmission(updated);
        } catch (ValidationException ex) {
            assertFalse(ex.getValidationError().isEmpty());
            assertEquals(LOWER_VERSION_ERROR, ex.getValidationError().get(0));
        }
    }
}
