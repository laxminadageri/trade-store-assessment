package com.assessment.tradestore.repository;

import com.assessment.tradestore.model.Trade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;

import static com.assessment.tradestore.utils.TestUtil.updatedTrade;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TradeRepositoryImplTest {

    @InjectMocks
    private TradeRepositoryImpl tradeRepository;

    @Test
    public void test_update_success() {
        tradeRepository.initializeTradeEntity();
        Trade existing = tradeRepository.getLatestTradeById("T1");
        Trade updated = updatedTrade("T1", 1);

        //assert existing trade before update call
        assertEquals("T1", existing.getTradeId());
        assertEquals(1, existing.getVersion());
        assertEquals("CP-1", existing.getCounterPartyId());
        assertEquals(LocalDate.of(2020, 5, 20), existing.getMaturityDate());
        assertEquals(LocalDate.now(), existing.getCreatedDate());
        assertEquals('N', existing.getExpired());

        tradeRepository.update(existing, updated);

        //assert existing trade after update call
        existing = tradeRepository.getLatestTradeById("T1");
        assertEquals("T1", existing.getTradeId());
        assertEquals(1, existing.getVersion());
        assertEquals("CP-2", existing.getCounterPartyId());
        assertEquals(LocalDate.now().plusDays(10), existing.getMaturityDate());
        assertEquals(LocalDate.now(), existing.getCreatedDate());
        assertEquals('N', existing.getExpired());
    }

    @Test
    public void test_getLatestTradeById() {
        tradeRepository.initializeTradeEntity();
        Trade trade = tradeRepository.getLatestTradeById("T2");

        assertEquals("T2", trade.getTradeId());
        assertEquals(2, trade.getVersion());
        assertEquals("B1", trade.getBookId());
        assertEquals(LocalDate.of(2021, 5, 20), trade.getMaturityDate());
        assertEquals(LocalDate.now(), trade.getCreatedDate());
        assertEquals('N', trade.getExpired());
    }

    @Test
    public void test_getByTradeIdAndVersion() {
        tradeRepository.initializeTradeEntity();
        Trade trade = tradeRepository.getLatestTradeById("T2");

        assertEquals("T2", trade.getTradeId());
        assertEquals(2, trade.getVersion());
        assertEquals("B1", trade.getBookId());
        assertEquals(LocalDate.of(2021, 5, 20), trade.getMaturityDate());
        assertEquals(LocalDate.now(), trade.getCreatedDate());
        assertEquals('N', trade.getExpired());
    }

    @Test
    public void test_updateExpiryFlag() {
        tradeRepository.initializeTradeEntity();

        Map trades = tradeRepository.getAllTrades();
        long expiredTrades = countExpiredTrades(trades);
        assertEquals(1, expiredTrades); // assert expired trades before updating expiry flag

        tradeRepository.updateExpiryFlag();

        trades = tradeRepository.getAllTrades();

        expiredTrades = countExpiredTrades(trades);
        assertEquals(4, expiredTrades); // assert expired trades after updating expiry flag
    }

    private long countExpiredTrades(Map trades) {
        return trades.values().stream()
                .filter(value -> ((Trade) value).getExpired() == 'Y')
                .count();
    }
}
