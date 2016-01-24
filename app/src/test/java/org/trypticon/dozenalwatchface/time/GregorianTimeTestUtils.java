package org.trypticon.dozenalwatchface.time;

import org.trypticon.dozenalwatchface.time.ClassicTime;

/**
 * Utilities for working with {@link ClassicTime} in tests.
 */
class GregorianTimeTestUtils {

    static ClassicTime buildTime(int year, int month, int dayOfMonth, int hour, int minute) {
        ClassicTime time = new ClassicTime();
        time.minute = minute;
        time.hour = hour;
        time.dayOfMonth = dayOfMonth;
        time.month = month;
        time.year = year;
        return time;
    }
}
