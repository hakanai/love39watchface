package org.trypticon.dozenalwatchface;

import org.trypticon.dozenalwatchface.time.Time;

/**
 * Abstraction of how to display time.
 */
abstract class TimeStyle {
    abstract Ticks getTicks();
    abstract Time getTime();
}
