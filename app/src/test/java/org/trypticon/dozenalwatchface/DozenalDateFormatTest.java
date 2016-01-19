package org.trypticon.dozenalwatchface;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DozenalDateFormat}.
 */
public class DozenalDateFormatTest {

    @Test
    public void testFirstDayOfYear() {
        DozenalDateFormat dateFormat = new DozenalDateFormat();
        dateFormat.updateLocale(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.updateTimeZone(TimeZone.getTimeZone("UTC"));
        time.setToDateTime(new DateTime(2015, 3, 1, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Sun 01 Mar")));
    }

    @Test
    public void testSkipMonday() {
        DozenalDateFormat dateFormat = new DozenalDateFormat();
        dateFormat.updateLocale(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.updateTimeZone(TimeZone.getTimeZone("UTC"));
        time.setToDateTime(new DateTime(2015, 3, 2, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Tue 02 Mar")));
    }

    @Test
    public void testLastDayOfYear() {
        DozenalDateFormat dateFormat = new DozenalDateFormat();
        dateFormat.updateLocale(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.updateTimeZone(TimeZone.getTimeZone("UTC"));
        time.setToDateTime(new DateTime(2015, 2, 28, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Fri 05 Int")));
    }

    @Test
    public void testLastDayOfLeapYear() {
        DozenalDateFormat dateFormat = new DozenalDateFormat();
        dateFormat.updateLocale(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.updateTimeZone(TimeZone.getTimeZone("UTC"));
        time.setToDateTime(new DateTime(2016, 2, 29, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Sat 06 Int")));
    }
}
