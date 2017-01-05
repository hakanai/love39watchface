package org.trypticon.android.love39watchface.time;

import org.junit.Test;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link ClassicTime}.
 */
public class ClassicTimeTest {
    private static final float EPSILON = 0.0000001f;

    @Test
    public void testDayOfWeek() {
        ClassicTime time = new ClassicTime(2016, 1, 27, 0, 0, 0);
        assertThat(time.getDayOfWeek(), is(3));
    }

    @Test
    public void testAngles_Midnight() {
        ClassicTime time = new ClassicTime(2016, 1, 1, 0, 0, 0);
        assertThat(time.getHourTurns() % 1.0f, is(0.0f));
        assertThat(time.getMinuteTurns() % 1.0f, is(0.0f));
        assertThat(time.getSecondTurns() % 1.0f, is(0.0f));
    }

    @Test
    public void testAngles_Midday() {
        ClassicTime time = new ClassicTime(2016, 1, 1, 12, 0, 0);
        assertThat(time.getHourTurns() % 1.0f, is(0.0f));
        assertThat(time.getMinuteTurns() % 1.0f, is(0.0f));
        assertThat(time.getSecondTurns() % 1.0f, is(0.0f));
    }

    @Test
    public void testAngles_3pm() {
        ClassicTime time = new ClassicTime(2016, 1, 1, 15, 0, 0);
        assertThat(time.getHourTurns() % 1.0f, is(1.0f/4.0f));
        assertThat(time.getMinuteTurns() % 1.0f, is(0.0f));
        assertThat(time.getSecondTurns() % 1.0f, is(0.0f));
    }

    @Test
    public void testAngles_HalfPast() {
        ClassicTime time = new ClassicTime(2016, 1, 1, 15, 30, 0);
        assertThat(time.getHourTurns() % 1.0, is(closeTo(7.0f / 24.0f, EPSILON)));
        assertThat(time.getMinuteTurns() % 1.0, is(closeTo(1.0f / 2.0f, EPSILON)));
        assertThat(time.getSecondTurns() % 1.0, is(closeTo(0.0f, EPSILON)));
    }
}
