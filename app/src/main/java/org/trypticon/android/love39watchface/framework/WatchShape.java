package org.trypticon.android.love39watchface.framework;

import android.graphics.Rect;
import android.view.WindowInsets;

/**
 * My replacement for the Clockwise class of the same name, since theirs did
 * not distinguish round screens from round screens with a "chin" at the bottom.
 */
public class WatchShape {
    private final boolean round;
    private final float chinRatio;

    private WatchShape(boolean round, float chinRatio) {
        this.round = round;
        this.chinRatio = chinRatio;
    }

    public static WatchShape forScreen(Rect screenBounds, WindowInsets screenInsets) {
        if (screenBounds.height() == 0) {
            throw new IllegalArgumentException("Screen is 0 size!");
        }
        return new WatchShape(
                screenInsets.isRound(),
                screenInsets.getSystemWindowInsetBottom() / (float) screenBounds.height());
    }

    public boolean isRound() {
        return round;
    }

    public float getChinRatio() {
        return chinRatio;
    }
}
