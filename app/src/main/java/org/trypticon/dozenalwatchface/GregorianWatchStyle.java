package org.trypticon.dozenalwatchface;

import android.content.Context;

import java.util.Locale;

/**
 * Gregorian watch face style.
 */
class GregorianWatchStyle extends WatchStyle {
    private Ticks ticks;
    private Time time;
    private DateFormat dateFormat;

    GregorianWatchStyle(Context context) {
        ticks = new GregorianTicks(context);
        time = new GregorianTime();
        dateFormat = new GregorianDateFormat(Locale.getDefault());
    }

    @Override
    Ticks getTicks() {
        return ticks;
    }

    @Override
    Time getTime() {
        return time;
    }

    @Override
    DateFormat getDateFormat() {
        return dateFormat;
    }
}
