package org.trypticon.android.love39watchface;

import android.content.Context;

/**
 * Hands for a dozenal clock.
 */
public class DozenalHands extends Hands {
    public DozenalHands(Context context) {
        super(context);
    }

    @Override
    boolean isZeroAtTop() {
        return false;
    }

    @Override
    boolean hasThirds() {
        return true;
    }
}
