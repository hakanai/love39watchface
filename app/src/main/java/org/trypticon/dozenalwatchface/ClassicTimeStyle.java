package org.trypticon.dozenalwatchface;

import android.content.Context;

import org.trypticon.dozenalwatchface.time.ClassicTime;
import org.trypticon.dozenalwatchface.time.Time;

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
