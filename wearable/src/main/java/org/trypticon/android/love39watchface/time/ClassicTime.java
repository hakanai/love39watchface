package org.trypticon.android.love39watchface.time;

import com.ustwo.clockwise.WatchFaceTime;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Stand-in utility for holding a gregorian time to avoid using any of Java's built-in horrors.
 */
class ClassicTime extends Time {
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MILLIS_PER_MINUTE = SECONDS_PER_MINUTE * MILLIS_PER_SECOND;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int MILLIS_PER_HOUR = MINUTES_PER_HOUR * MILLIS_PER_MINUTE;
    private static final int HOURS_PER_HALF_DAY = 12;
    private static final int MILLIS_PER_HALF_DAY = HOURS_PER_HALF_DAY * MILLIS_PER_HOUR;

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

    ClassicTime() {
    }

    ClassicTime(int year, int monthOfYear, int dayOfMonth, int hour, int minute, int second) {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        setDayOfMonth(dayOfMonth);
        setMonthOfYear(monthOfYear);
        setYear(year);
        recomputeDate();
        recomputeTime();
    }

    @Override
    DateFormat createDateFormat(Locale locale) {
        return new ClassicDateFormat(locale);
    }

    @Override
    public void setTo(WatchFaceTime time) {
        setYear(time.year);
        setMonthOfYear(time.month + 1); // WatchFaceTime inherits this weirdness
        setDayOfMonth(time.monthDay);
        setDayOfWeek(time.weekDay);
        hour = time.hour;
        minute = time.minute;
        second = time.second;
        millisecond = time.millis;
        recomputeTime();
    }

    void setTo(ClassicTime time) {
        setYear(time.getYear());
        setMonthOfYear(time.getMonthOfYear());
        setDayOfMonth(time.getDayOfMonth());
        setDayOfWeek(time.getDayOfWeek());
        hour = time.hour;
        minute = time.minute;
        second = time.second;
        millisecond = time.millisecond;
        recomputeTime();
    }

    @Override
    public void setToSample() {
        setYear(2015);
        setMonthOfYear(3);
        setDayOfMonth(9);
        hour = 2;
        minute = 50;
        second = 20;
        millisecond = 0;
        recomputeDate();
        recomputeTime();
    }

    @Override
    public void setYear(int year) {
        super.setYear(year);
    }

    @Override
    public void setMonthOfYear(int monthOfYear) {
        super.setMonthOfYear(monthOfYear);
    }

    @Override
    public void setDayOfMonth(int dayOfMonth) {
        super.setDayOfMonth(dayOfMonth);
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

    private void recomputeDate() {
        temp.set(Calendar.YEAR, getYear());
        temp.set(Calendar.MONTH, getMonthOfYear() - 1);
        temp.set(Calendar.DAY_OF_MONTH, getDayOfMonth());
        setDayOfWeek(temp.get(Calendar.DAY_OF_WEEK) - 1);
    }

    private void recomputeTime() {
        int minuteMillis = millisecond + second * MILLIS_PER_SECOND;
        secondTurns = minuteMillis / (float) MILLIS_PER_MINUTE;

        int hourMillis = minuteMillis + minute * MILLIS_PER_MINUTE;
        minuteTurns = hourMillis / (float) MILLIS_PER_HOUR;

        int halfDayMillis = hourMillis + (hour % HOURS_PER_HALF_DAY) * MILLIS_PER_HOUR;
        hourTurns = halfDayMillis / (float) MILLIS_PER_HALF_DAY;
    }
}
