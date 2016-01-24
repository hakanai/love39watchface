package org.trypticon.android.love39watchface.time;

import com.ustwo.clockwise.WatchFaceTime;

/**
 * Abstraction of time systems.
 */
public abstract class Time {
    public abstract void setTo(WatchFaceTime time);

    abstract int getYear();
    abstract int getMonth();
    abstract int getDayOfMonth();
    abstract int getDayOfWeek();

    public abstract float getHourTurns();
    public abstract float getMinuteTurns();
    public abstract float getSecondTurns();
    public abstract float getThirdTurns();
    public abstract boolean hasThirds();

    abstract long getEpochMillis();
}
