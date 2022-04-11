package com.crypto.demo.service;

import com.crypto.demo.model.Interval;
import com.crypto.demo.model.Candlestick;
import com.crypto.demo.model.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CandlestickService {

    public List<Candlestick> buildCandlesticks(List<Trade> trades, Interval interval) {
        Map<Long, List<Trade>> groupedByInterval = trades.stream().collect(Collectors.groupingBy(t -> getIntervalStartTime(t.getTimestamp(), interval)));

        return groupedByInterval
                .entrySet()
                .stream()
                .map(g -> buildCandlestick(g.getKey(), g.getValue()))
                .sorted(Comparator.comparing(Candlestick::getTimestamp))
                .collect(Collectors.toList());
    }

    public long getIntervalStartTime(long timestamp, Interval interval) {

        switch (interval) {
            case SEVEN_DAYS: {
                // We assume that SEVEN_DAYS interval starts from Monday.
                var time = LocalDateTime.ofEpochSecond(Math.floorDiv(timestamp, 1000), 0, ZoneOffset.UTC);
                var dayOfWeek = time.getDayOfWeek().getValue();
                var previousMondayDate = time.minusDays(dayOfWeek - 1);

                return LocalDateTime.of(
                                previousMondayDate.getYear(),
                                previousMondayDate.getMonth(),
                                previousMondayDate.getDayOfMonth(),
                                0,
                                0,
                                0).
                        toEpochSecond(ZoneOffset.UTC) * 1000;
            }
            // TODO: Insufficient business logic knowledge when interval starts
            //            case TWO_WEEKS: {}
            case ONE_MONTH: {
                // We assume that ONE_MONTH interval starts from 1st of the month.
                var time = LocalDateTime.ofEpochSecond(Math.floorDiv(timestamp, 1000), 0, ZoneOffset.UTC);

                return LocalDateTime.of(
                                time.getYear(),
                                time.getMonth(),
                                1,
                                0,
                                0,
                                0).
                        toEpochSecond(ZoneOffset.UTC) * 1000;
            }
            default:
                long intervalMs = interval.getMillis();

                return Math.floorDiv(timestamp, intervalMs) * intervalMs;

        }
    }

    private Candlestick buildCandlestick(long timestamp, List<Trade> trades) {

        return new Candlestick(
                timestamp,
                Collections.min(trades, Comparator.comparing(Trade::getTimestamp)).getPrice(),
                Collections.max(trades, Comparator.comparing(Trade::getPrice)).getPrice(),
                Collections.min(trades, Comparator.comparing(Trade::getPrice)).getPrice(),
                Collections.max(trades, Comparator.comparing(Trade::getTimestamp)).getPrice(),
                trades.stream().map(Trade::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add)
        );

    }
}
