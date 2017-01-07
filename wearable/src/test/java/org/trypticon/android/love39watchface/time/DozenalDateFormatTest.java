/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.time;

import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DozenalDateFormat}.
 */
public class DozenalDateFormatTest {

    @Test
    public void testFirstDayOfYear() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 1, 0, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Sun, 01 Mar")));
    }

    @Test
    public void testDigit10() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 10, 0, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Thu, 0\u218A Mar")));
    }

    @Test
    public void testDigit11() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 11, 0, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Fri, 0\u218B Mar")));
    }

    @Test
    public void testSkipMonday() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 2, 0, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Tue, 02 Mar")));
    }

    @Test
    public void testLastDayOfYear() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 2, 28, 0, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Fri, 05 Int")));
    }

    @Test
    public void testLastDayOfLeapYear() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2016, 2, 29, 0, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Sat, 06 Int")));
    }
}
