package org.trypticon.dozenalwatchface;

import android.content.Context;

import java.util.Locale;

/**
 * Dozenal watch face style.
 */
class DozenalWatchStyle extends WatchStyle {
    private Ticks ticks;
    private Time time;
    private DateFormat dateFormat;

    DozenalWatchStyle(Context context) {
        ticks = new DozenalTicks(context);
        time = new DozenalTime();
        dateFormat = new DozenalDateFormat(Locale.getDefault());
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
