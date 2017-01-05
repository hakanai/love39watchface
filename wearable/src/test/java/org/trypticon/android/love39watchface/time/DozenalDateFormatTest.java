package org.trypticon.android.love39watchface.time;

import org.junit.Test;

import java.text.FieldPosition;
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
        StringBuffer buffer = new StringBuffer(64);
        FieldPosition fieldPosition = new FieldPosition(java.text.DateFormat.MONTH_FIELD);
        dateFormat.formatDate(time, buffer, fieldPosition);
        assertThat(buffer.toString(), is(equalTo("Sun, 01 Mar")));
    }

    @Test
    public void testDigit10() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 10, 0, 0, 0));
        StringBuffer buffer = new StringBuffer(64);
        FieldPosition fieldPosition = new FieldPosition(java.text.DateFormat.MONTH_FIELD);
        dateFormat.formatDate(time, buffer, fieldPosition);
        assertThat(buffer.toString(), is(equalTo("Thu, 0\u218A Mar")));
    }

    @Test
    public void testDigit11() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 11, 0, 0, 0));
        StringBuffer buffer = new StringBuffer(64);
        FieldPosition fieldPosition = new FieldPosition(java.text.DateFormat.MONTH_FIELD);
        dateFormat.formatDate(time, buffer, fieldPosition);
        assertThat(buffer.toString(), is(equalTo("Fri, 0\u218B Mar")));
    }

    @Test
    public void testSkipMonday() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 3, 2, 0, 0, 0));
        StringBuffer buffer = new StringBuffer(64);
        FieldPosition fieldPosition = new FieldPosition(java.text.DateFormat.MONTH_FIELD);
        dateFormat.formatDate(time, buffer, fieldPosition);
        assertThat(buffer.toString(), is(equalTo("Tue, 02 Mar")));
    }

    @Test
    public void testLastDayOfYear() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2015, 2, 28, 0, 0, 0));
        StringBuffer buffer = new StringBuffer(64);
        FieldPosition fieldPosition = new FieldPosition(java.text.DateFormat.MONTH_FIELD);
        dateFormat.formatDate(time, buffer, fieldPosition);
        assertThat(buffer.toString(), is(equalTo("Fri, 05 Int")));
    }

    @Test
    public void testLastDayOfLeapYear() {
        DozenalDateFormat dateFormat = new DozenalDateFormat(Locale.ENGLISH);
        DozenalTime time = new DozenalTime();
        time.setTo(new ClassicTime(2016, 2, 29, 0, 0, 0));
        StringBuffer buffer = new StringBuffer(64);
        FieldPosition fieldPosition = new FieldPosition(java.text.DateFormat.MONTH_FIELD);
        dateFormat.formatDate(time, buffer, fieldPosition);
        assertThat(buffer.toString(), is(equalTo("Sat, 06 Int")));
    }
}
