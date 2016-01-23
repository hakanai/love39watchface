package org.trypticon.dozenalwatchface;

import com.ustwo.clockwise.WatchFaceTime;

/**
 * Abstraction of time systems.
 */
abstract class Time {
    abstract void setTo(WatchFaceTime time);

    abstract int getYear();
    abstract int getMonth();
    abstract int getDayOfMonth();
    abstract int getDayOfWeek();

    abstract float getHourTurns();
    abstract float getMinuteTurns();
    abstract float getSecondTurns();
    abstract float getThirdTurns();
    abstract boolean hasThirds();

    abstract long getEpochMillis();
}
