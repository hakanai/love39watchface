package org.trypticon.dozenalwatchface;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

/**
 * Encapsulates dozenal time calculation.
 */
class DozenalTime {
    // First offset is never used.
    private static final int[] YEAR_OFFSETS = { 42, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static final int[] DAY_OFFSETS = {
            42,
            306, 337,   0,  31,  61,  92,
            122, 153, 184, 214, 245, 275,
            };
    private static final int DAYS_PER_MONTH = 30;
    private static final int DAYS_PER_WEEK = 6;

    private static final int GREGORIAN_MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

    private static final int HOURS_PER_DAY = 12;
    private static final int MINUTES_PER_HOUR = 12;
    private static final int SECONDS_PER_MINUTE = 12;
    private static final int THIRDS_PER_SECOND = 12;

    private DateTime dateTime = new DateTime();

    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    private int hourOfDay;
    private int minuteOfHour;
    private int secondOfMinute;
    private int thirdOfSecond;
    private float hourTurns;
    private float minuteTurns;
    private float secondTurns;
    private float thirdTurns;

    public int getYear() {
        return year;
    }

    //1..13
    public int getMonth() {
        return month;
    }

    //1..30
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    //0..5
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    //0..11
    int getHourOfDay() {
        return hourOfDay;
    }

    //0..11
    int getMinuteOfHour() {
        return minuteOfHour;
    }

    //0..11
    int getSecondOfMinute() {
        return secondOfMinute;
    }

    //0..11
    int getThirdOfSecond() {
        return thirdOfSecond;
    }

    float getHourTurns() {
        return hourTurns;
    }

    float getMinuteTurns() {
        return minuteTurns;
    }

    float getSecondTurns() {
        return secondTurns;
    }

    float getThirdTurns() {
        return thirdTurns;
    }

    void setToNow() {
        setToDateTime(new DateTime());
    }

    void setToDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
        recompute();
    }

    void updateTimeZone(TimeZone timeZone) {
        updateTimeZone(DateTimeZone.forTimeZone(timeZone));
    }

    void updateTimeZone(DateTimeZone timeZone) {
        dateTime = dateTime.withZone(timeZone);
        recompute();
    }

    private void recompute() {
        int gregorianYear = dateTime.getYear();
        int gregorianMonthOfYear = dateTime.getMonthOfYear();
        int gregorianDayOfMonth = dateTime.getDayOfMonth();

        year = gregorianYear + YEAR_OFFSETS[gregorianMonthOfYear];
        int dayOfYear = DAY_OFFSETS[gregorianMonthOfYear] + gregorianDayOfMonth - 1;
        month = (dayOfYear / DAYS_PER_MONTH) + 1;
        dayOfMonth = (dayOfYear % DAYS_PER_MONTH) + 1;
        dayOfWeek = dayOfYear % DAYS_PER_WEEK;

        // We do recompute this from the calendar, because sometimes an hour in the day
        // is missing. So this gives us the effect of getting free daylight savings support.
        int dayMillis = dateTime.getMillisOfDay();

        int tmp = dayMillis / (GREGORIAN_MILLIS_PER_DAY / (12 * 12 * 12 * 12));
        thirdOfSecond = tmp % 12;
        tmp /= 12;
        secondOfMinute = tmp % 12;
        tmp /= 12;
        minuteOfHour = tmp % 12;
        tmp /= 12;
        hourOfDay = tmp;

        hourTurns = dayMillis / (float) GREGORIAN_MILLIS_PER_DAY;
        minuteTurns = hourTurns * DozenalTime.MINUTES_PER_HOUR;
        secondTurns = minuteTurns * DozenalTime.SECONDS_PER_MINUTE;
        thirdTurns = secondTurns * DozenalTime.THIRDS_PER_SECOND;
    }
}
