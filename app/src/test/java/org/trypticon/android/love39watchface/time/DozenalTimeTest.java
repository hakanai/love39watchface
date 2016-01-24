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
        time.setTo(ClassicTimeTestUtils.buildTime(2015, 3, 1, 0, 0));
        assertThat(time.getYear(), is(2015));
        assertThat(time.getMonth(), is(1));
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
        time.setTo(ClassicTimeTestUtils.buildTime(2015, 2, 28, 0, 0));
        assertThat(time.getYear(), is(2014));
        assertThat(time.getMonth(), is(13));
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
        time.setTo(ClassicTimeTestUtils.buildTime(2016, 2, 29, 0, 0));
        assertThat(time.getYear(), is(2015));
        assertThat(time.getMonth(), is(13));
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
        time.setTo(ClassicTimeTestUtils.buildTime(2015, 3, 1, 12, 0));
        assertThat(time.getHourOfDay(), is(6));
        assertThat(time.getMinuteOfHour(), is(0));
        assertThat(time.getSecondOfMinute(), is(0));
        assertThat(time.getThirdOfSecond(), is(0));
    }

    @Test
    public void testHalfMorning() {
        DozenalTime time = new DozenalTime();
        time.setTo(ClassicTimeTestUtils.buildTime(2015, 3, 1, 6, 0));
        assertThat(time.getHourOfDay(), is(3));
        assertThat(time.getMinuteOfHour(), is(0));
        assertThat(time.getSecondOfMinute(), is(0));
        assertThat(time.getThirdOfSecond(), is(0));
    }
}
