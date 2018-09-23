/*
 * Copyright © 2016-2018 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.time;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import java.text.FieldPosition;
import java.util.Locale;

/**
 * Date formatter for Gregorian dates.
 */
public class ClassicDateFormat extends DateFormat {
    private final android.icu.text.DateFormat delegate;
    private final CalendarAdapter calendar = new CalendarAdapter();

    @SuppressLint("SimpleDateFormat") // We got the pattern using a legit method.
    ClassicDateFormat(Locale locale) {
        String pattern = android.text.format.DateFormat.getBestDateTimePattern(locale, "EEEdMMM");
        if (pattern == null) { // when running tests. :(
            pattern = "EEE, d MMM";
        }

        delegate = new SimpleDateFormat(pattern, locale);
        delegate.setCalendar(calendar);
    }

    @Override
    public String formatDate(Time time) {
        calendar.set(Calendar.DAY_OF_WEEK, time.getDayOfWeek() + 1);
        calendar.set(Calendar.DAY_OF_MONTH, time.getDayOfMonth());
        // Android hard-codes in a + 1 when formatting as a number.
        calendar.set(Calendar.MONTH, time.getMonthOfYear() - 1);
        return delegate.format(calendar, new StringBuffer(20), new FieldPosition(0))
                .toString();
    }

    // Never serialised.
    @SuppressWarnings("serial")
    private class CalendarAdapter extends Calendar {
        private static final long serialVersionUID = 1;

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
            // No-op, we set the values already and want to avoid recomputing them.
        }

        @Override
        protected int handleGetLimit(int i, int i1) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected int handleComputeMonthStart(int i, int i1, boolean b) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected int handleGetExtendedYear() {
            throw new UnsupportedOperationException();
        }
    }
}
