package org.trypticon.android.love39watchface;

import org.trypticon.android.love39watchface.time.Time;

/**
 * Abstraction of how to display time.
 */
public abstract class TimeStyle {
    public abstract Ticks getTicks();
    public abstract Hands getHands();
    public abstract Time getTime();
}
