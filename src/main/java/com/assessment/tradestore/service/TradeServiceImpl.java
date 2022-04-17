package com.assessment.tradestore.service;

import com.assessment.tradestore.exception.ValidationException;
import com.assessment.tradestore.model.Trade;
import com.assessment.tradestore.repository.TradeRepository;
import com.assessment.tradestore.validations.ValidationResult;
import com.assessment.tradestore.validations.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private ValidationService validationService;
    @Autowired
    private TradeRepository tradeRepository;

    @Override
    public void tradeTransmission(Trade updatedTrade) {
        Trade existingTrade = tradeRepository.getLatestTradeById(updatedTrade.getTradeId());
        ValidationResult result = validationService.validateTradeTransmission(existingTrade, updatedTrade);
        if (result.hasErrors()) {
            throw new ValidationException(result.getErrors());
        }
        //if incoming trade has same version then update the existing trade
        if (existingTrade.getVersion() == updatedTrade.getVersion()) {
            tradeRepository.update(existingTrade, updatedTrade);
        }
    }

    @Override
    @Scheduled(cron = "0 5 * * * *") //schedules a job everyday at 5 AM
    public void updateExpiryFlagScheduler() {
        tradeRepository.updateExpiryFlag();
    }
}
