package org.trypticon.dozenalwatchface;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Encapsulates dozenal time calculation.
 */
class DozenalTime {
    private static final int[] YEAR_OFFSETS = { -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static final int[] DAY_OFFSETS = {
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

    private final GregorianCalendar calculator = new GregorianCalendar();

    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    private float hourTurns;
    private float minuteTurns;
    private float secondTurns;
    private float thirdTurns;

    public int getYear() {
        return year;
    }

    //0..12
    public int getMonth() {
        return month;
    }

    //0..29
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    //0..5
    public int getDayOfWeek() {
        return dayOfWeek;
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
        calculator.setTime(new Date());
        recompute();
    }

    void updateTimeZone(TimeZone timeZone) {
        calculator.setTimeZone(timeZone);
        recompute();
    }

    private void recompute() {
        int gregorianYear = calculator.get(Calendar.YEAR);
        int gregorianMonthOfYear = calculator.get(Calendar.MONTH);
        int gregorianDayOfMonth = calculator.get(Calendar.DAY_OF_MONTH);

        year = gregorianYear + YEAR_OFFSETS[gregorianMonthOfYear];
        int dayOfYear = DAY_OFFSETS[gregorianMonthOfYear] + gregorianDayOfMonth;
        month = dayOfYear / DAYS_PER_MONTH;
        dayOfMonth = dayOfYear % DAYS_PER_MONTH;
        dayOfWeek = dayOfYear % DAYS_PER_WEEK;

        // We do recompute this from the calendar, because sometimes an hour in the day
        // is missing. So this gives us the effect of getting free daylight savings support.
        int dayMillis = calculator.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 +
                calculator.get(Calendar.MINUTE) * 60 * 1000 +
                calculator.get(Calendar.SECOND) * 1000 +
                calculator.get(Calendar.MILLISECOND);

        hourTurns = dayMillis / (float) GREGORIAN_MILLIS_PER_DAY;
        minuteTurns = hourTurns * DozenalTime.MINUTES_PER_HOUR;
        secondTurns = minuteTurns * DozenalTime.SECONDS_PER_MINUTE;
        thirdTurns = secondTurns * DozenalTime.THIRDS_PER_SECOND;
    }
}
