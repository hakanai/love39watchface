package org.trypticon.android.love39watchface;

import org.trypticon.android.love39watchface.time.DateFormat;
import org.trypticon.android.love39watchface.time.Time;

/**
 * Abstraction of how to display the date.
 */
abstract class DateStyle {
    abstract DateFormat getDateFormat();
    abstract Time getTime();
}
