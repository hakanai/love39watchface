package org.trypticon.android.love39watchface;

import android.content.Context;

/**
 * Hands for a Gregorian clock.
 */
public class ClassicHands extends Hands {
    public ClassicHands(Context context) {
        super(context);
    }

    @Override
    boolean isZeroAtTop() {
        return true;
    }

    @Override
    boolean hasThirds() {
        return false;
    }
}
