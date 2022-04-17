package com.assessment.tradestore.repository;

import com.assessment.tradestore.model.Trade;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class TradeRepositoryImpl implements TradeRepository {

    Map<TradeKey, Trade> trades = new HashMap<>();

    @PostConstruct
    public void initializeTradeEntity() {
        trades.put(new TradeKey("T1", 1), new Trade("T1", 1, "CP-1", "B1", LocalDate.of(2020, 5,
                20), LocalDate.now(), 'N'));
        trades.put(new TradeKey("T2", 2), new Trade("T2", 2, "CP-2", "B1", LocalDate.of(2021, 5,
                20), LocalDate.now(), 'N'));
        trades.put(new TradeKey("T2", 1), new Trade("T2", 1, "CP-1", "B1", LocalDate.of(2021, 5,
                20), LocalDate.of(2015, 3, 14), 'N'));
        trades.put(new TradeKey("T3", 3), new Trade("T3", 3, "CP-3", "B2", LocalDate.of(2014, 5,
                20), LocalDate.now(), 'Y'));
    }

    @Override
    public void update(Trade existing, Trade updated) {
        trades.put(new TradeKey(existing.getTradeId(), existing.getVersion()), updated);
    }

    @Override
    public Trade getLatestTradeById(String tradeId) {
        return trades.values().stream()
                .filter(trade -> trade.getTradeId().equals(tradeId))
                .reduce((t1, t2) -> t1.getVersion() > t2.getVersion() ? t1 : t2)
                .orElse(null);
    }

    @Override
    public Map<TradeKey, Trade> getAllTrades() {
        return trades;
    }

    @Override
    public void updateExpiryFlag() {
        for(Map.Entry<TradeKey, Trade> entry : trades.entrySet()) {
            Trade tradeEntry = entry.getValue();
            if(tradeEntry.getMaturityDate().isBefore(LocalDate.now())) {
                tradeEntry.setExpired('Y');
                trades.put(new TradeKey(tradeEntry.getTradeId(), tradeEntry.getVersion()), tradeEntry);
            }
        }
    }

    private static class TradeKey {
        String tradeId;
        int version;

        public TradeKey(String tradeId, int version) {
            this.tradeId = tradeId;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TradeKey tradeKey = (TradeKey) o;
            return version == tradeKey.version && Objects.equals(tradeId, tradeKey.tradeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tradeId, version);
        }
    }
}
