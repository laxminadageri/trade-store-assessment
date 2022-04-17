package com.assessment.tradestore.service;

import com.assessment.tradestore.model.Trade;

public interface TradeService {
    void tradeTransmission(Trade trade);
    void updateExpiryFlagScheduler();
}
