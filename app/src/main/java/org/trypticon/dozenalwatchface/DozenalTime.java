package org.trypticon.dozenalwatchface;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Encapsulates dozenal time calculation.
 */
class DozenalTime {
    private static final int GREGORIAN_MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

    private static final int HOURS_PER_DAY = 12;
    private static final int MINUTES_PER_HOUR = 12;
    private static final int SECONDS_PER_MINUTE = 12;
    private static final int THIRDS_PER_SECOND = 12;

    private final GregorianCalendar calculator = new GregorianCalendar();

    private float hourTurns;
    private float minuteTurns;
    private float secondTurns;
    private float thirdTurns;

    // TODO: 18/10/15 Fill these in when we want them.
//    int getDayOfMonth() {
//        return calculator.get(Calendar.DAY_OF_MONTH);
//    }


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
