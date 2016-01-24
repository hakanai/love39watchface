package org.trypticon.dozenalwatchface;

import android.content.Context;

import org.trypticon.dozenalwatchface.time.DozenalTime;
import org.trypticon.dozenalwatchface.time.Time;

/**
 * Dozenal time display.
 */
class DozenalTimeStyle extends TimeStyle {
    private final Ticks ticks;
    private final Time time;

    DozenalTimeStyle(Context context) {
        ticks = new DozenalTicks(context);
        time = new DozenalTime();
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
