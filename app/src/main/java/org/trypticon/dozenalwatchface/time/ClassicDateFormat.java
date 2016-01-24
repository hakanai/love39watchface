package org.trypticon.dozenalwatchface.time;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date formatter for Gregorian dates.
 */
public class ClassicDateFormat extends DateFormat {
    private final java.text.DateFormat delegate;

    @SuppressLint("SimpleDateFormat") // We got the pattern using a legit method.
    public ClassicDateFormat(Locale locale) {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(locale, "EEEddMMM");
        delegate = new SimpleDateFormat(pattern);
    }

    @Override
    public String formatDate(Time time) {
        long millis = time.getEpochMillis();
        return delegate.format(new Date(millis));
    }
}
