package com.crypto.demo;

import com.crypto.demo.api.CryptoApiClient;
import com.crypto.demo.model.Candlestick;
import com.crypto.demo.model.Interval;
import com.crypto.demo.service.CandlestickService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CryptoTradeApiValidationTests {

    String baseUrl = "https://uat-api.3ona.co";
    CryptoApiClient cryptoApiClient;
    CandlestickService candlestickService;

    @BeforeEach
    void beforeAll() {
        candlestickService = new CandlestickService();
        cryptoApiClient = new CryptoApiClient(baseUrl);
    }

    @Test
    void validateBtcUsdt1mTrades() {
        var instrumentName = "BTC_USDT";
        var interval = Interval.ONE_MINUTE;

        var trades = cryptoApiClient.getTrades(instrumentName).block().getResult().getData();

        var candlesticksResponse = cryptoApiClient.getCandlestick(instrumentName, interval).block().getResult().getData();

        var candlesticksToCompare = candlestickService.buildCandlesticks(trades, interval);

        Assertions.assertEquals(candlesticksToCompare.size(), candlesticksResponse.size());

        for (var i = 0; i < candlesticksResponse.size(); i++) {
            var calculatedCandlestick = candlesticksToCompare.get(i);
            var receivedCandlestick = candlesticksResponse.get(i);

            Assertions.assertEquals(calculatedCandlestick.getTimestamp(), receivedCandlestick.getTimestamp());
            Assertions.assertEquals(0, calculatedCandlestick.getOpenPrice().compareTo(receivedCandlestick.getOpenPrice()));
            Assertions.assertEquals(0, calculatedCandlestick.getClosePrice().compareTo(receivedCandlestick.getClosePrice()));
            Assertions.assertEquals(0, calculatedCandlestick.getHighPrice().compareTo(receivedCandlestick.getHighPrice()));
            Assertions.assertEquals(0, calculatedCandlestick.getLowPrice().compareTo(receivedCandlestick.getLowPrice()));
            Assertions.assertEquals(0, calculatedCandlestick.getVolume().compareTo(receivedCandlestick.getVolume()));
        }

    }

    @ParameterizedTest
    @MethodSource("getValidateAllInstrumentsByAllIntervalsArguments")
    void validateAllInstrumentsByAllIntervals(String instrumentName, Interval interval, boolean validateMissingTrades) {

        var trades = cryptoApiClient.getTrades(instrumentName).block().getResult().getData();

        var candlesticksResponse = cryptoApiClient.getCandlestick(instrumentName, interval).block().getResult().getData();

        var candlesticksToCompare = candlestickService.buildCandlesticks(trades, interval);

        Assertions.assertEquals(
                candlesticksResponse.size(),
                candlesticksResponse.stream().collect(Collectors.groupingBy(Candlestick::getTimestamp)).size(),
                String.format("Only one Candlestick should exist for specific period for %s on interval %s", instrumentName, interval.getLabel()));

        if (validateMissingTrades) {
            Assertions.assertEquals(
                    candlesticksResponse.size(),
                    candlesticksToCompare.size(),
                    String.format("Candlestick count doesn't match  for %s on interval %s", instrumentName, interval.getLabel()));
        }

        for (Candlestick calculatedCandlestick : candlesticksToCompare) {
            var matchedReceivedCandlestick = candlesticksResponse.stream().filter(e -> e.getTimestamp() == calculatedCandlestick.getTimestamp()).collect(Collectors.toList());

            Assertions.assertEquals(
                    1,
                    matchedReceivedCandlestick.size(),
                    String.format("Cannot match candlestick for %s on interval %s", instrumentName, interval.getLabel()));

            var receivedCandlestick = matchedReceivedCandlestick.get(0);

            Assertions.assertEquals(
                    calculatedCandlestick.getTimestamp(),
                    receivedCandlestick.getTimestamp(),
                    String.format("Candlestick timestamp differs for %s on interval %s", instrumentName, interval.getLabel()));

            Assertions.assertEquals(calculatedCandlestick.getOpenPrice(), receivedCandlestick.getOpenPrice(),
                    String.format("Candlestick open price differs for %s on interval %s", instrumentName, interval.getLabel()));
            Assertions.assertEquals(calculatedCandlestick.getClosePrice().toString(), receivedCandlestick.getClosePrice().toString(),
                    String.format("Candlestick close price differs for %s on interval %s", instrumentName, interval.getLabel()));
            Assertions.assertEquals(calculatedCandlestick.getHighPrice().toString(), receivedCandlestick.getHighPrice().toString(),
                    String.format("Candlestick high price differs for %s on interval %s", instrumentName, interval.getLabel()));
            Assertions.assertEquals(calculatedCandlestick.getLowPrice().toString(), receivedCandlestick.getLowPrice().toString(),
                    String.format("Candlestick low price differs for %s on interval %s", instrumentName, interval.getLabel()));
            Assertions.assertEquals(calculatedCandlestick.getVolume().toString(), receivedCandlestick.getVolume().toString(),
                    String.format("Candlestick volume differs for %s on interval %s", instrumentName, interval.getLabel()));

        }
    }


    private static Stream<Arguments> getValidateAllInstrumentsByAllIntervalsArguments() {
        var allInstruments = List.of("BTC_USDT", "CRO_USDT");

        var intervals = List.of(
                Interval.ONE_MINUTE,
                Interval.FIVE_MINUTES,
                Interval.FIFTEEN_MINUTES,
                Interval.THIRTY_MINUTES,
                Interval.ONE_HOUR,
                Interval.FOUR_HOURS,
                Interval.SIX_HOURS,
                Interval.TWELVE_HOUR,
                Interval.ONE_DAY,
                Interval.SEVEN_DAYS,
                // TODO: TWO_WEEKS interval is not supported yet.
                // Interval.TWO_WEEKS,
                Interval.ONE_MONTH
        );

        var validateMissingTradesOptions = List.of(true, false);

        return allInstruments.stream().flatMap(instrumentName ->
                intervals.stream().flatMap(interval ->
                        validateMissingTradesOptions.stream().map(validateMissingTrades ->
                                arguments(instrumentName, interval, validateMissingTrades)))
        );
    }

}
