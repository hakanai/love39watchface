package org.trypticon.android.love39watchface.time;

import com.ustwo.clockwise.WatchFaceTime;

/**
 * Encapsulates dozenal time calculation.
 */
public class DozenalTime extends Time {
    private static final int[] YEAR_OFFSETS = {
            42, // never used
            -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static final int[] DAY_OFFSETS = {
            42, // never used
            306, 337,   0,  31,  61,  92,
            122, 153, 184, 214, 245, 275,
            };
    private static final int DAYS_PER_MONTH = 30;
    private static final int DAYS_PER_WEEK = 6;

    private static final int GREGORIAN_MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

    private static final int MILLIS_PER_DOZENAL_HOUR = GREGORIAN_MILLIS_PER_DAY / 12;
    private static final int MILLIS_PER_DOZENAL_MINUTE = MILLIS_PER_DOZENAL_HOUR / 12;
    private static final int MILLIS_PER_DOZENAL_SECOND = MILLIS_PER_DOZENAL_MINUTE / 12;
    private static final float MILLIS_PER_DOZENAL_THIRD = (float) MILLIS_PER_DOZENAL_SECOND / 12;

    private static final int MINUTE_TURNS_PER_HOUR_TURN = 12;
    private static final int SECOND_TURNS_PER_MINUTE_TURN = 12;
    private static final int THIRD_TURNS_PER_SECOND_TURN = 12;

    private ClassicTime time = new ClassicTime();

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

    @Override
    int getYear() {
        return year;
    }

    //1..13
    @Override
    int getMonth() {
        return month;
    }

    //1..30
    @Override
    int getDayOfMonth() {
        return dayOfMonth;
    }

    //0..5
    @Override
    int getDayOfWeek() {
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
        return thirdTurns;
    }

    @Override
    public boolean hasThirds() {
        return true;
    }

    @Override
    public void setTo(WatchFaceTime time) {
        this.time.setTo(time);
        recompute();
    }

    void setTo(ClassicTime time) {
        this.time.setTo(time);
        recompute();
    }

    @Override
    public void setToSample() {
        ClassicTime time = new ClassicTime();
        time.year = 2015;
        time.month = 3;
        time.dayOfMonth = 9;
        time.hour = 16;
        time.minute = 41;
        time.second = 30;
        time.millisecond = 667;
        setTo(time);
    }

    private void recompute() {
        int gregorianYear = time.year;
        int gregorianMonthOfYear = time.month;
        int gregorianDayOfMonth = time.dayOfMonth;

        year = gregorianYear + YEAR_OFFSETS[gregorianMonthOfYear];
        int dayOfYear = DAY_OFFSETS[gregorianMonthOfYear] + gregorianDayOfMonth - 1;
        month = (dayOfYear / DAYS_PER_MONTH) + 1;
        dayOfMonth = (dayOfYear % DAYS_PER_MONTH) + 1;
        dayOfWeek = dayOfYear % DAYS_PER_WEEK;

        int dayMillis = ((time.hour * 60 + time.minute) * 60 + time.second) * 1000 + time.millisecond;

        hourOfDay = dayMillis / MILLIS_PER_DOZENAL_HOUR;
        int tmp = dayMillis % MILLIS_PER_DOZENAL_HOUR;
        minuteOfHour = tmp / MILLIS_PER_DOZENAL_MINUTE;
        tmp %= MILLIS_PER_DOZENAL_MINUTE;
        secondOfMinute = tmp / MILLIS_PER_DOZENAL_SECOND;
        tmp %= MILLIS_PER_DOZENAL_SECOND;
        thirdOfSecond = (int) (tmp / MILLIS_PER_DOZENAL_THIRD);

        hourTurns = dayMillis / (float) GREGORIAN_MILLIS_PER_DAY;
        minuteTurns = hourTurns * MINUTE_TURNS_PER_HOUR_TURN;
        secondTurns = minuteTurns * SECOND_TURNS_PER_MINUTE_TURN;
        thirdTurns = secondTurns * THIRD_TURNS_PER_SECOND_TURN;
    }
}
