/*
 * Copyright © 2016-2018 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.time;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DozenalTime}.
 */
public class DozenalTimeTest {

    @Test
    public void testMidnightAtStartOfYear() {
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 1, 0, 0, 0));
        assertThat(time.getYear(), is(2015));
        assertThat(time.getMonthOfYear(), is(1));
        assertThat(time.getDayOfMonth(), is(1));
        assertThat(time.getDayOfWeek(), is(0));
        assertThat(time.getHourOfDay(), is(0));
        assertThat(time.getMinuteOfHour(), is(0));
        assertThat(time.getSecondOfMinute(), is(0));
        assertThat(time.getThirdOfSecond(), is(0));
    }

    @Test
    public void testMidnightAtEndOfYear() {
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 2, 28, 0, 0, 0));
        assertThat(time.getYear(), is(2014));
        assertThat(time.getMonthOfYear(), is(13));
        assertThat(time.getDayOfMonth(), is(5));
        assertThat(time.getDayOfWeek(), is(4));
        assertThat(time.getHourOfDay(), is(0));
        assertThat(time.getMinuteOfHour(), is(0));
        assertThat(time.getSecondOfMinute(), is(0));
        assertThat(time.getThirdOfSecond(), is(0));
    }

    @Test
    public void testMidnightAtEndOfLeapYear() {
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2016, 2, 29, 0, 0, 0));
        assertThat(time.getYear(), is(2015));
        assertThat(time.getMonthOfYear(), is(13));
        assertThat(time.getDayOfMonth(), is(6));
        assertThat(time.getDayOfWeek(), is(5));
        assertThat(time.getHourOfDay(), is(0));
        assertThat(time.getMinuteOfHour(), is(0));
        assertThat(time.getSecondOfMinute(), is(0));
        assertThat(time.getThirdOfSecond(), is(0));
    }

    @Test
    public void testMidday() {
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 1, 12, 0, 0));
        assertThat(time.getHourOfDay(), is(6));
        assertThat(time.getMinuteOfHour(), is(0));
        assertThat(time.getSecondOfMinute(), is(0));
        assertThat(time.getThirdOfSecond(), is(0));
    }

    @Test
    public void testHalfMorning() {
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 1, 6, 0, 0));
        assertThat(time.getHourOfDay(), is(3));
        assertThat(time.getMinuteOfHour(), is(0));
        assertThat(time.getSecondOfMinute(), is(0));
        assertThat(time.getThirdOfSecond(), is(0));
    }
}
