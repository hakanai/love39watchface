package org.trypticon.dozenalwatchface;

/**
 * Utilities for working with {@link GregorianTime} in tests.
 */
class GregorianTimeTestUtils {

    static GregorianTime buildTime(int year, int month, int dayOfMonth, int hour, int minute) {
        GregorianTime time = new GregorianTime();
        time.minute = minute;
        time.hour = hour;
        time.dayOfMonth = dayOfMonth;
        time.month = month;
        time.year = year;
        return time;
    }
}
