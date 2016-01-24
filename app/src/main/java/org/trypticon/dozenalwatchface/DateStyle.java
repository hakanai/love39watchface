package org.trypticon.dozenalwatchface;

import org.trypticon.dozenalwatchface.time.DateFormat;
import org.trypticon.dozenalwatchface.time.Time;

/**
 * Abstraction of how to display the date.
 */
abstract class DateStyle {
    abstract DateFormat getDateFormat();
    abstract Time getTime();
}
