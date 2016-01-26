package org.trypticon.android.love39watchface;

import android.content.Context;

import org.trypticon.android.love39watchface.time.DozenalTime;
import org.trypticon.android.love39watchface.time.Time;

/**
 * Dozenal time display.
 */
public class DozenalTimeStyle extends TimeStyle {
    private final Ticks ticks;
    private final Hands hands;
    private final Time time;

    public DozenalTimeStyle(Context context) {
        ticks = new DozenalTicks(context);
        hands = new DozenalHands(context);
        time = new DozenalTime();
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
