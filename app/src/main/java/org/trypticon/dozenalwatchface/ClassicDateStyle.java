package org.trypticon.dozenalwatchface;

import android.content.Context;

import org.trypticon.dozenalwatchface.time.DateFormat;
import org.trypticon.dozenalwatchface.time.ClassicDateFormat;
import org.trypticon.dozenalwatchface.time.ClassicTime;
import org.trypticon.dozenalwatchface.time.Time;

import java.util.Locale;

/**
 * Classic date display.
 */
class ClassicDateStyle extends DateStyle {
    private final DateFormat dateFormat;
    private final Time time;

    ClassicDateStyle(Context context) {
        dateFormat = new ClassicDateFormat(Locale.getDefault());
        time = new ClassicTime();
    }

    @Override
    DateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    Time getTime() {
        return time;
    }
}
