package org.trypticon.dozenalwatchface;

import android.annotation.SuppressLint;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Formatter for dozenal dates.
 */
class DozenalDateFormat extends DateFormat {
    private final java.text.DateFormat delegate;
    private final CalendarAdapter calendar = new CalendarAdapter();
    private final Date dummyDate = new Date(0);

    @SuppressLint("SimpleDateFormat") // We got the pattern in a localised way
    DozenalDateFormat(Locale locale) {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(locale, "EEEddMMM");
        if (pattern == null) { // when running tests. :(
            pattern = "EEE, dd MMM";
        }

        delegate = new SimpleDateFormat(pattern, new DateFormatSymbolsAdapter(locale));
        delegate.setCalendar(calendar);
    }

    @Override
    String formatDate(Time time) {
        calendar.set(Calendar.DAY_OF_WEEK, time.getDayOfWeek());
        calendar.set(Calendar.DAY_OF_MONTH, time.getDayOfMonth());
        // Android hard-codes in a + 1 when formatting as a number.
        calendar.set(Calendar.MONTH, time.getMonth() - 1);
        return delegate.format(dummyDate);
    }

    private class DateFormatSymbolsAdapter extends DateFormatSymbols {
        private DateFormatSymbolsAdapter(Locale locale) {
            super(locale);

            // Here we use the setters instead of overriding the getters, because Android has a bug
            // where it pokes at the private fields of DateFormatSymbols instead of calling the
            // appropriate getters.

            String[] gregorianWeekdays = super.getShortWeekdays();
            String[] shortWeekdays = new String[6];
            System.arraycopy(gregorianWeekdays, 1, shortWeekdays, 0, 1);
            System.arraycopy(gregorianWeekdays, 3, shortWeekdays, 1, 5); // skip Monday
            setShortWeekdays(shortWeekdays);

            String[] gregorianMonths = super.getShortMonths();
            String[] shortMonths = new String[13];
            System.arraycopy(gregorianMonths, 2, shortMonths, 0, 10);
            System.arraycopy(gregorianMonths, 0, shortMonths, 10, 2);
            shortMonths[12] = "Int"; //TODO: Localise this
            setShortMonths(shortMonths);
        }
    }

    private class CalendarAdapter extends Calendar {
        @Override
        public void add(int field, int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void computeFields() {
            // No-op, we set the values already and want to avoid recomputing them.
        }

        @Override
        protected void computeTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getGreatestMinimum(int field) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getLeastMaximum(int field) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getMaximum(int field) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getMinimum(int field) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void roll(int field, boolean increment) {
            throw new UnsupportedOperationException();
        }
    }
}
