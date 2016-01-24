package org.trypticon.android.love39watchface;

import android.content.Context;

import org.trypticon.android.love39watchface.time.ClassicTime;
import org.trypticon.android.love39watchface.time.Time;

/**
 * Classic time display.
 */
class ClassicTimeStyle extends TimeStyle {
    private final Ticks ticks;
    private final Time time;

    ClassicTimeStyle(Context context) {
        ticks = new ClassicTicks(context);
        time = new ClassicTime();
    }

    @Override
    Ticks getTicks() {
        return ticks;
    }

    @Override
    Time getTime() {
        return time;
    }
}
