package org.trypticon.dozenalwatchface;

import java.util.Locale;

/**
 * Date formatter for Gregorian dates.
 */
class GregorianDateFormat extends DateFormat {
    private final String format;

    GregorianDateFormat(Locale locale) {
        format = android.text.format.DateFormat.getBestDateTimePattern(locale, "EEEddMMM");
    }

    @Override
    String formatDate(Time time) {
        long millis = time.getEpochMillis();
        return android.text.format.DateFormat.format(format, millis).toString();
    }
}
