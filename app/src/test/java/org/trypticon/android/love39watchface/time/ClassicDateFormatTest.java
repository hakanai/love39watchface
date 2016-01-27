package org.trypticon.android.love39watchface.time;

import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link ClassicDateFormat}.
 */
public class ClassicDateFormatTest {

    @Test
    public void test() {
        ClassicDateFormat dateFormat = new ClassicDateFormat(Locale.ENGLISH);
        ClassicTime time = new ClassicTime();
        time.setTo(new ClassicTime(2016, 1, 27, 0, 0, 0));
        assertThat(dateFormat.formatDate(time), is(equalTo("Wed, 27 Jan")));
    }
}
