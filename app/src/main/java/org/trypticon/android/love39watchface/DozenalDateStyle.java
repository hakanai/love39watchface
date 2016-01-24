package org.trypticon.android.love39watchface;

import android.content.Context;

import org.trypticon.android.love39watchface.time.DateFormat;
import org.trypticon.android.love39watchface.time.DozenalDateFormat;
import org.trypticon.android.love39watchface.time.DozenalTime;
import org.trypticon.android.love39watchface.time.Time;

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
