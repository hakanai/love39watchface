package org.trypticon.android.love39watchface.time;

import com.ustwo.clockwise.WatchFaceTime;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Stand-in utility for holding a gregorian time to avoid using any of Java's built-in horrors.
 */
public class ClassicTime extends Time {
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MILLIS_PER_MINUTE = SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int MILLIS_PER_HOUR = MINUTES_PER_HOUR * MILLIS_PER_MINUTE;
    private static final int HOURS_PER_HALF_DAY = 12;
    private static final int MILLIS_PER_HALF_DAY = HOURS_PER_HALF_DAY * MILLIS_PER_HOUR;

    // AD year
    int year;

    // 1..12
    int month;

    // 1..31
    int dayOfMonth;

    // 0..6
    int dayOfWeek;

    // 0..23
    int hour;

    // 0..59
    int minute;

    // 0..59
    int second;

    // 0..999
    int millisecond;

    private float hourTurns;
    private float minuteTurns;
    private float secondTurns;

    private final Calendar temp = new GregorianCalendar();

    public ClassicTime() {
    }

    public ClassicTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
        recompute();
    }

    @Override
    public void setTo(WatchFaceTime time) {
        year = time.year;
        month = time.month + 1; // WatchFaceTime inherits this weirdness
        dayOfMonth = time.monthDay;
        dayOfWeek = time.weekDay;
        hour = time.hour;
        minute = time.minute;
        second = time.second;
        millisecond = time.millis;
        recompute();
    }

    void setTo(ClassicTime time) {
        year = time.year;
        month = time.month;
        dayOfMonth = time.dayOfMonth;
        dayOfWeek = time.dayOfWeek;
        hour = time.hour;
        minute = time.minute;
        second = time.second;
        millisecond = time.millisecond;
        recompute();
    }

    @Override
    public void setToSample() {
        year = 2015;
        month = 3;
        dayOfMonth = 9;
        dayOfWeek = 3;
        hour = 2;
        minute = 50;
        second = 20;
        millisecond = 0;
        recompute();
    }

    @Override
    int getYear() {
        return year;
    }

    @Override
    int getMonth() {
        return month;
    }

    @Override
    int getDayOfMonth() {
        return dayOfMonth;
    }

    @Override
    int getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public float getHourTurns() {
        return hourTurns;
    }

    @Override
    public float getMinuteTurns() {
        return minuteTurns;
    }

    @Override
    public float getSecondTurns() {
        return secondTurns;
    }

    @Override
    public float getThirdTurns() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasThirds() {
        return false;
    }

    private void recompute() {
        temp.set(Calendar.YEAR, year);
        temp.set(Calendar.MONTH, month - 1);
        temp.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dayOfWeek = temp.get(Calendar.DAY_OF_WEEK) - 1;

        int minuteMillis = millisecond + second * MILLIS_PER_SECOND;
        secondTurns = minuteMillis / (float) MILLIS_PER_MINUTE;

        int hourMillis = minuteMillis + minute * MILLIS_PER_MINUTE;
        minuteTurns = hourMillis / (float) MILLIS_PER_HOUR;

        int halfDayMillis = hourMillis + (hour % HOURS_PER_HALF_DAY) * MILLIS_PER_HOUR;
        hourTurns = halfDayMillis / (float) MILLIS_PER_HALF_DAY;
    }
}
