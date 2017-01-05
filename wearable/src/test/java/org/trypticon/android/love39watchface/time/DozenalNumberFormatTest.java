package org.trypticon.android.love39watchface.time;

import org.junit.Test;

import java.text.NumberFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link DozenalNumberFormat}.
 */
public class DozenalNumberFormatTest {

    @Test
    public void testZero() {
        assertThat(new DozenalNumberFormat().format(0), is(equalTo("0")));
    }

    @Test
    public void testTen() {
        assertThat(new DozenalNumberFormat().format(10), is(equalTo("\u218A")));
    }

    @Test
    public void testDozen() {
        assertThat(new DozenalNumberFormat().format(12), is(equalTo("10")));
    }

    @Test
    public void testGross() {
        assertThat(new DozenalNumberFormat().format(144), is(equalTo("100")));
    }

    @Test
    public void testPadding() {
        NumberFormat format = new DozenalNumberFormat();
        format.setMinimumIntegerDigits(2);
        assertThat(format.format(3), is(equalTo("03")));
    }
}
