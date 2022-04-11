package com.crypto.demo.service;

import com.crypto.demo.model.Interval;
import com.crypto.demo.model.Candlestick;
import com.crypto.demo.model.Side;
import com.crypto.demo.model.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CandlestickServiceTests {

    CandlestickService service;

    @BeforeEach
    void before() {
        service = new CandlestickService();
    }

    @Test
    void buildCandlesticksFromTrades5mIntervalTest() {

        Trade t1 = createTrade(25.0, 1, 100, LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 12));
        Trade t2 = createTrade(75.0, 2, 5.5, LocalDateTime.of(2022, Month.APRIL, 7, 11, 32, 12));
        Trade t3 = createTrade(50.0, 3, 16, LocalDateTime.of(2022, Month.APRIL, 7, 11, 32, 42));
        Trade t4 = createTrade(25.0, 4, 54, LocalDateTime.of(2022, Month.APRIL, 7, 11, 37, 12));
        Trade t5 = createTrade(26.0, 5, 12, LocalDateTime.of(2022, Month.APRIL, 7, 11, 39, 42));

        List<Trade> trades = List.of(t1, t2, t3, t4, t5);
        List<Candlestick> result = service.buildCandlesticks(trades, Interval.FIVE_MINUTES);

        assertEquals(2, result.size(), "returns a list of two candlesticks");
        assertEquals(
                localDateTimeToTimestamp(LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 0)),
                result.get(0).getTimestamp(),
                "Should return candlestick start time is 1649327400 or 2022-04-07 11:30:00");
        assertEquals(
                localDateTimeToTimestamp(LocalDateTime.of(2022, Month.APRIL, 7, 11, 35, 0)),
                result.get(1).getTimestamp(),
                "Should return candlestick start time is 1649327700 or 2022-04-07 11:35:00");

    }

    @Test
    void buildCandlesticksFromTradesNoTradesTest() {

        List<Trade> trades = List.of();
        List<Candlestick> result = service.buildCandlesticks(trades, Interval.ONE_MINUTE);

        assertEquals(0, result.size(), "Should return empty list");

    }

    @Test
    void buildCandlesticksOneTradeTest() {

        Trade t1 = createTrade(25.0, 1, 15.56, LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 12));

        List<Trade> trades = List.of(t1);
        List<Candlestick> result = service.buildCandlesticks(trades, Interval.FIVE_MINUTES);

        assertEquals(1, result.size(), "Should return one candlestick");
        var candlestick = result.get(0);

        assertEquals(
                localDateTimeToTimestamp(LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 0)),
                candlestick.getTimestamp(),
                "Should return candlestick start time is 1649327400000 or 2022-04-07 11:30:00");

        assertEquals(BigDecimal.valueOf(25.0), candlestick.getLowPrice(), "Should return lowest price 25.0");
        assertEquals(BigDecimal.valueOf(25.0), candlestick.getHighPrice(), "Should return highest price 25.0");
        assertEquals(BigDecimal.valueOf(25.0), candlestick.getOpenPrice(), "Should return open price 25.0");
        assertEquals(BigDecimal.valueOf(25.0), candlestick.getClosePrice(), "Should return close price 25.0");
        assertEquals(BigDecimal.valueOf(15.56), candlestick.getVolume(), "Should return volume 15.56");

    }

    @Test
    void buildCandlesticksTwoTradesInSameIntervalTest() {

        Trade t1 = createTrade(30.0, 1, 10, LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 12));
        Trade t2 = createTrade(25.0, 2, 22.5, LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 19));

        List<Trade> trades = List.of(t1, t2);
        List<Candlestick> result = service.buildCandlesticks(trades, Interval.ONE_MINUTE);

        assertEquals(1, result.size(), "Should return one candlestick");
        var candlestick = result.get(0);

        assertEquals(
                localDateTimeToTimestamp(LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 0)),
                candlestick.getTimestamp(),
                "Should return candlestick start time is 1649327400000 or 2022-04-07 11:30:00");

        assertEquals(BigDecimal.valueOf(25.0), candlestick.getLowPrice(), "Should return lowest price 25.0");
        assertEquals(BigDecimal.valueOf(30.0), candlestick.getHighPrice(), "Should return highest price 30.0");
        assertEquals(BigDecimal.valueOf(30.0), candlestick.getOpenPrice(), "Should return open price 30.0");
        assertEquals(BigDecimal.valueOf(25.0), candlestick.getClosePrice(), "Should return close price 25.0");
        assertEquals(BigDecimal.valueOf(32.5), candlestick.getVolume(), "Should return volume price 32.5");

    }

    @Test
    void buildCandlesticksThreeTradesInSameIntervalTest() {

        Trade t1 = createTrade(30.0, 1, 7, LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 12));
        Trade t2 = createTrade(25.0, 2, 11.11, LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 19));
        Trade t3 = createTrade(35.0, 3, 5, LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 49));

        List<Trade> trades = List.of(t1, t2, t3);
        List<Candlestick> result = service.buildCandlesticks(trades, Interval.FIVE_MINUTES);

        assertEquals(1, result.size(), "Should return one candlestick");
        var candlestick = result.get(0);

        assertEquals(
                localDateTimeToTimestamp(LocalDateTime.of(2022, Month.APRIL, 7, 11, 30, 0)),
                candlestick.getTimestamp(),
                "Should return candlestick start time is 1649327400000 or 2022-04-07 11:30:00");

        assertEquals(BigDecimal.valueOf(25.0), candlestick.getLowPrice(), "Should return lowest price 25.0");
        assertEquals(BigDecimal.valueOf(35.0), candlestick.getHighPrice(), "Should return highest price 35.0");
        assertEquals(BigDecimal.valueOf(30.0), candlestick.getOpenPrice(), "Should return open price 30.0");
        assertEquals(BigDecimal.valueOf(35.0), candlestick.getClosePrice(), "Should return close price 35.0");
        assertEquals(BigDecimal.valueOf(23.11), candlestick.getVolume(), "Should return volume 23.11");

    }


    @ParameterizedTest
    @MethodSource("getIntervalTestData")
    void getIntervalStartTimeTest(LocalDateTime time, Interval interval, LocalDateTime expected) {
        var timestamp = localDateTimeToTimestamp(time);
        long result = service.getIntervalStartTime(timestamp, interval);
        assertEquals(localDateTimeToTimestamp(expected), result, "Start time doesn't match");
    }

    private static Stream<Arguments> getIntervalTestData() {
        return Stream.of(
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 0),
                        Interval.ONE_MINUTE,
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.ONE_MINUTE,
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.FIVE_MINUTES,
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 30, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.FIFTEEN_MINUTES,
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 30, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.THIRTY_MINUTES,
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 30, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.ONE_HOUR,
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 0, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.FOUR_HOURS,
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 0, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.SIX_HOURS,
                        LocalDateTime.of(2022, Month.APRIL, 2, 6, 0, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.TWELVE_HOUR,
                        LocalDateTime.of(2022, Month.APRIL, 2, 0, 0, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.ONE_DAY,
                        LocalDateTime.of(2022, Month.APRIL, 2, 0, 0, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.SEVEN_DAYS,
                        LocalDateTime.of(2022, Month.MARCH, 28, 0, 0, 0)),
// TODO: TWO_WEEKS interval is not supported yet.
//                arguments(
//                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
//                        Interval.TWO_WEEKS,
//                        LocalDateTime.of(2022, Month.APRIL, 1, 0, 0, 0)),
                arguments(
                        LocalDateTime.of(2022, Month.APRIL, 2, 8, 31, 12),
                        Interval.ONE_MONTH,
                        LocalDateTime.of(2022, Month.APRIL, 1, 0, 0, 0))
        );
    }

    private long localDateTimeToTimestamp(LocalDateTime time) {
        return time.atOffset(ZoneOffset.UTC).toEpochSecond() * 1000;
    }

    private Trade createTrade(double price, int id, double quantity, LocalDateTime time) {
        var timestamp = localDateTimeToTimestamp(time);

        return new Trade(
                "BTC_USDT",
                BigDecimal.valueOf(price),
                BigDecimal.valueOf(quantity),
                Side.BUY,
                BigInteger.valueOf(id),
                timestamp
        );
    }
}
