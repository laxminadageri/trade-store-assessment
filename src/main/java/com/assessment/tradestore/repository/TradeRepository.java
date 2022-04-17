package com.assessment.tradestore.repository;

import com.assessment.tradestore.model.Trade;

import java.util.Map;

public interface TradeRepository {
    //update the trade if incoming trade has same version
    void update(Trade existing, Trade updated);

    //returns higher version trade of given trade id
    Trade getLatestTradeById(String tradeId);

    Map getAllTrades();

    void updateExpiryFlag();
}
