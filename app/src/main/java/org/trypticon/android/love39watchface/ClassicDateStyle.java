package org.trypticon.android.love39watchface;

import android.content.Context;

import org.trypticon.android.love39watchface.time.DateFormat;
import org.trypticon.android.love39watchface.time.ClassicDateFormat;
import org.trypticon.android.love39watchface.time.ClassicTime;
import org.trypticon.android.love39watchface.time.Time;

import java.util.Locale;

/**
 * Classic date display.
 */
public class ClassicDateStyle extends DateStyle {
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
