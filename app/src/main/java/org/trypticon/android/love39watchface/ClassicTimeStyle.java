package org.trypticon.android.love39watchface;

import android.content.Context;

import org.trypticon.android.love39watchface.time.ClassicTime;
import org.trypticon.android.love39watchface.time.Time;

/**
 * Classic time display.
 */
public class ClassicTimeStyle extends TimeStyle {
    private final Ticks ticks;
    private final Hands hands;
    private final Time time;

    public ClassicTimeStyle(Context context) {
        ticks = new ClassicTicks(context);
        hands = new ClassicHands(context);
        time = new ClassicTime();
    }

    @Override
    public Ticks getTicks() {
        return ticks;
    }

    @Override
    public Hands getHands() {
        return hands;
    }

    @Override
    public Time getTime() {
        return time;
    }
}
