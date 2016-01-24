package org.trypticon.android.love39watchface;

import org.trypticon.android.love39watchface.time.Time;

/**
 * Abstraction of how to display time.
 */
abstract class TimeStyle {
    abstract Ticks getTicks();
    abstract Time getTime();
}
