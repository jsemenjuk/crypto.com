package com.crypto.demo;

import com.crypto.demo.model.Interval;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntervalTests {

    @Test
    void testGetByLabel() {
        Assertions.assertEquals(Interval.ONE_MINUTE, Interval.getByLabel("1m"));
        Assertions.assertEquals(Interval.ONE_DAY, Interval.getByLabel("1D"));
        Assertions.assertEquals(Interval.ONE_MONTH, Interval.getByLabel("1M"));
    }

    @Test
    void testGetByLabelWithUnknownValue() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Interval.getByLabel("1d"));
        Assertions.assertEquals("Unsupported label: 1d", exception.getMessage());
    }

    @Test
    void testMillisCalc() {
        Assertions.assertEquals(Interval.ONE_MINUTE.getMillis(), 60_000L);
        Assertions.assertEquals(Interval.ONE_HOUR.getMillis(), 3_600_000L);
        Assertions.assertEquals(Interval.ONE_DAY.getMillis(), 86_400_000L);
        Assertions.assertEquals(Interval.ONE_MONTH.getMillis(), 2_592_000_000L);
    }
}
