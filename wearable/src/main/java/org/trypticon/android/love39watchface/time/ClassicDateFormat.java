package org.trypticon.android.love39watchface.time;

import android.annotation.SuppressLint;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Date formatter for Gregorian dates.
 */
public class ClassicDateFormat extends DateFormat {
    private final java.text.DateFormat delegate;
    private final CalendarAdapter calendar = new CalendarAdapter();
    private final Date dummyDate = new Date(0);

    @SuppressLint("SimpleDateFormat") // We got the pattern using a legit method.
    public ClassicDateFormat(Locale locale) {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(locale, "EEEddMMM");
        if (pattern == null) { // when running tests. :(
            pattern = "EEE, dd MMM";
        }

        delegate = new SimpleDateFormat(pattern);
        delegate.setCalendar(calendar);
    }

    @Override
    public void formatDate(Time time, StringBuffer buffer, FieldPosition fieldPosition) {
        calendar.set(Calendar.DAY_OF_WEEK, time.getDayOfWeek() + 1);
        calendar.set(Calendar.DAY_OF_MONTH, time.getDayOfMonth());
        // Android hard-codes in a + 1 when formatting as a number.
        calendar.set(Calendar.MONTH, time.getMonth() - 1);
        delegate.format(dummyDate, buffer, fieldPosition);
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
