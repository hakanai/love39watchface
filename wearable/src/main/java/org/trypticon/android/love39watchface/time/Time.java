/*
 * Copyright © 2016-2018 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.time;

import com.ustwo.clockwise.WatchFaceTime;

import java.util.Locale;

/**
 * Abstraction of time systems.
 */
public abstract class Time {
    public abstract void setTo(WatchFaceTime time);

    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private int dayOfWeek;

    private final DateFormat dateFormat = createDateFormat(Locale.getDefault());
    private String formattedDate = "";
    private boolean formattedDateDirty = true;

    int getYear() {
        return year;
    }

    void setYear(int year) {
        if (this.year != year) {
            this.year = year;
            formattedDateDirty = true;
        }
    }

    int getMonthOfYear() {
        return monthOfYear;
    }

    void setMonthOfYear(int monthOfYear) {
        if (this.monthOfYear != monthOfYear) {
            this.monthOfYear = monthOfYear;
            formattedDateDirty = true;
        }
    }

    int getDayOfMonth() {
        return dayOfMonth;
    }

    void setDayOfMonth(int dayOfMonth) {
        if (this.dayOfMonth != dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
            formattedDateDirty = true;
        }
    }

    int getDayOfWeek() {
        return dayOfWeek;
    }

    void setDayOfWeek(int dayOfWeek) {
        if (this.dayOfWeek != dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            formattedDateDirty = true;
        }
    }

    abstract DateFormat createDateFormat(Locale locale);

    public String getFormattedDate() {
        if (formattedDateDirty) {
            formattedDate = dateFormat.formatDate(this);
            formattedDateDirty = false;
        }
        return formattedDate;
    }

    //@TestOnly
    public String getFormattedDate(Locale locale) {
        return createDateFormat(locale).formatDate(this);
    }

    public abstract float getHourTurns();
    public abstract float getMinuteTurns();
    public abstract float getSecondTurns();
    public abstract float getThirdTurns();
    public abstract boolean hasThirds();

    public abstract void setToSample();
}
