package com.crypto.demo.model;

import lombok.Getter;

@Getter
public enum Interval{
    ONE_MINUTE("1m", 60 * Constants.ONE_SECOND_MS),
    FIVE_MINUTES("5m", 5 * Constants.ONE_MINUTE_MS),
    FIFTEEN_MINUTES("15m", 15 * Constants.ONE_MINUTE_MS),
    THIRTY_MINUTES("30m", 30 * Constants.ONE_MINUTE_MS),
    ONE_HOUR("1h", Constants.ONE_HOUR_MS),
    FOUR_HOURS("4h", 4 * Constants.ONE_HOUR_MS),
    SIX_HOURS("6h", 6 * Constants.ONE_HOUR_MS),
    TWELVE_HOUR("12h", 12 * Constants.ONE_HOUR_MS),
    ONE_DAY("1D", Constants.ONE_DAY_MS),
    SEVEN_DAYS("7D", 7 * Constants.ONE_DAY_MS),
    TWO_WEEKS("14D", 14 * Constants.ONE_DAY_MS),
    ONE_MONTH("1M", Constants.ONE_MONTH_MS);

    private final String label;
    private final long millis;

    Interval(String label, long millis) {
        this.label = label;
        this.millis = millis;
    }

    public static Interval getByLabel(String label) {
        for(Interval e : values()) {
            if(e.label.equals(label)) return e;
        }

        throw new IllegalArgumentException("Unsupported label: " + label);
    }

    private static class Constants {
        public static final long ONE_SECOND_MS = 1000;
        public static final long ONE_MINUTE_MS = 60 * ONE_SECOND_MS;
        public static final long ONE_HOUR_MS = 60 * ONE_MINUTE_MS;
        public static final long ONE_DAY_MS = 24 * ONE_HOUR_MS;
        public static final long ONE_MONTH_MS = 30 * ONE_DAY_MS;
    }
}
