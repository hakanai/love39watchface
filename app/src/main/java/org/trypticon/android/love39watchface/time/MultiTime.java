package org.trypticon.android.love39watchface.time;

import com.ustwo.clockwise.WatchFaceTime;

/**
 * A container for multiple measures of time.
 * Basically exists so that we can avoid object creation overhead.
 */
public class MultiTime {
    private final ClassicTime classicTime = new ClassicTime();
    private final DozenalTime dozenalTime = new DozenalTime();

    public void setTo(WatchFaceTime time) {
        classicTime.setTo(time);
        dozenalTime.setTo(time);
    }

    public Time getTime(TimeSystem timeSystem) {
        switch (timeSystem) {
            case CLASSIC:
                return classicTime;
            case DOZENAL:
                return dozenalTime;
            default:
                throw new IllegalStateException("Unimplemented time system: " + timeSystem);
        }
    }
}
