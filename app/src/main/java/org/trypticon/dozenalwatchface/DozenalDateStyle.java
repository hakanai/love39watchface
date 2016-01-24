package org.trypticon.dozenalwatchface;

import android.content.Context;

import org.trypticon.dozenalwatchface.time.DateFormat;
import org.trypticon.dozenalwatchface.time.DozenalDateFormat;
import org.trypticon.dozenalwatchface.time.DozenalTime;
import org.trypticon.dozenalwatchface.time.Time;

import java.util.Locale;

/**
 * Dozenal date display.
 */
class DozenalDateStyle extends DateStyle {
    private final DateFormat dateFormat;
    private final Time time;

    DozenalDateStyle(Context context) {
        dateFormat = new DozenalDateFormat(Locale.getDefault());
        time = new DozenalTime();
    }

    @Override
    DateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public Time getTime() {
        return time;
    }
}
