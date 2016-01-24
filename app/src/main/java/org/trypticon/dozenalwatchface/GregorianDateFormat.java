package org.trypticon.dozenalwatchface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date formatter for Gregorian dates.
 */
class GregorianDateFormat extends DateFormat {
    private final java.text.DateFormat delegate;

    GregorianDateFormat(Locale locale) {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(locale, "EEEddMMM");
        delegate = new SimpleDateFormat(pattern);
    }

    @Override
    String formatDate(Time time) {
        long millis = time.getEpochMillis();
        return delegate.format(new Date(millis));
    }
}
