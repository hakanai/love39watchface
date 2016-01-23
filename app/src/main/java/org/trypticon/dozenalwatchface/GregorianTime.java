package org.trypticon.dozenalwatchface;

import com.ustwo.clockwise.WatchFaceTime;

/**
 * Stand-in utility for holding a gregorian time to avoid using any of Java's built-in horrors.
 */
class GregorianTime {
    // AD year
    int year;

    // 1..12
    int month;

    // 1..31
    int dayOfMonth;

    // 0..23
    int hour;

    // 0..59
    int minute;

    // 0..59
    int second;

    // 0..999
    int millisecond;

    void setTo(WatchFaceTime time) {
        year = time.year;
        month = time.month + 1; // WatchFaceTime inherits this weirdness
        dayOfMonth = time.monthDay;
        hour = time.hour;
        minute = time.minute;
        second = time.second;
    }

    void setTo(GregorianTime time) {
        year = time.year;
        month = time.month;
        dayOfMonth = time.dayOfMonth;
        hour = time.hour;
        minute = time.minute;
        second = time.second;
    }

    public int computeDayMillis() {
        return hour * 60 * 60 * 1000 +
                minute * 60 * 1000 +
                second * 1000 +
                millisecond;
    }
}
