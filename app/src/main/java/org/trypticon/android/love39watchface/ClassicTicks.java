package org.trypticon.android.love39watchface;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Ticks for a Gregorian clock.
 */
class ClassicTicks extends Ticks {
    private static final int SUBDIVISIONS = 5;

    ClassicTicks(Context context) {
        super(context, SUBDIVISIONS);
    }

    @Override
    boolean isZeroAtTop() {
        return true;
    }

    @Override
    Drawable[] getDigits(Context context) {
        return new Drawable[] {
                context.getDrawable(R.drawable.digit0),
                context.getDrawable(R.drawable.digit1),
                context.getDrawable(R.drawable.digit2),
                context.getDrawable(R.drawable.digit3),
                context.getDrawable(R.drawable.digit4),
                context.getDrawable(R.drawable.digit5),
                context.getDrawable(R.drawable.digit6),
                context.getDrawable(R.drawable.digit7),
                context.getDrawable(R.drawable.digit8),
                context.getDrawable(R.drawable.digit9),
                context.getDrawable(R.drawable.digit10),
                context.getDrawable(R.drawable.digit11),
        };
    }
}
